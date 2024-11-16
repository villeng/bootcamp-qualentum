import base64
import sys
from collections import defaultdict
from pathlib import Path

from dataclasses_json import dataclass_json
from dataclasses import dataclass, field
from typing import List
from argparse import ArgumentParser, Namespace
import logging
import requests

logger = logging.getLogger(__name__)
logger.setLevel(logging.WARNING)
handler = logging.StreamHandler()
logger.addHandler(handler)

@dataclass_json
@dataclass
class Address:
    city: str
    state: str

@dataclass_json
@dataclass
class User:
    first_name: str
    last_name: str
    address: Address
    city: str  = field(init=False)
    state: str  = field(init=False)

    def __post_init__(self):
        self.city = self.address.city
        self.state = self.address.state

    def to_list(self) -> list:
        return [
            self.first_name,
            self.last_name,
            self.address.city,
            self.address.state
        ]


def parse_args(args: List) -> Namespace:
    """
    Parse command line arguments for authentication.
    
    Args:
        args (List): Command line arguments
        
    Returns:
        Namespace: Parsed arguments containing username and password
    """
    parser = ArgumentParser(description="Our first program")
    parser.add_argument(
        '-u', 
        '--username', 
        type=str, 
        required=True
    )
    parser.add_argument(
        '-p', 
        '--password', 
        type=str, 
        required=True
    )
    parser.add_argument(
        '-v',
        '-verbose',
        action='store_true'
    )
    parser.add_argument(
        '-c',
        '-count',
        type=int
    )
    return parser.parse_args(args)


def get_auth_header(username: str, password: str) -> str:
    """
    Generate Basic Authentication header from username and password.
    
    Args:
        username (str): User's username
        password (str): User's password
        
    Returns:
        str: Formatted Basic Authentication header
    """
    encoded_auth_string = f"{username}:{password}".encode('ascii')
    b64_auth_string = base64.b64encode(encoded_auth_string)
    return f"Basic {b64_auth_string.decode('ascii')}"

def get_dataapi_data(username: str, password: str, count: int) -> dict:
    """
    Fetch data from httpbin.org using Basic Authentication.
    
    Args:
        username (str): Username for authentication
        password (str): Password for authentication
        count (int): Number of data requests (users) to fetch
        
    Returns:
        dict: JSON response from the server
        
    Raises:
        RuntimeError: If the server response is not successful

    """
    endpoint = f"https://random-data-api.com/api/v2/users?size={count}&is_xml=false"
    headers = {
        'Accept': 'application/json',
        'Authorization': get_auth_header(username, password)
    }

    logger.debug(f"Requesting information for n={count} users.")
    response = requests.get(endpoint, headers=headers)
    if response.ok:
        logger.debug("Response OK")
        return response.json()
    
    raise RuntimeError("Unable to get response from server")


def parse_response(response):
    logger.debug("Parsing response from https://random-data-api.com/ ...")
    users_data = []
    for user_data in response:
        user = User.from_dict(user_data)
        users_data.append(user.to_list())
    logger.debug(users_data)
    return users_data


def print_results(listed_user_data):
    for user in listed_user_data:
        print(user)


def list_users_by_state(listed_user_data):
    users_by_state = defaultdict(list)
    for user in listed_user_data:
        state = user[3]
        users_by_state[state].append(user)
    logger.debug("Users listed by state: ")
    logger.debug(users_by_state)

    return users_by_state


def save_users_by_state(listed_users_by_state, output_dir):
    logger.debug("Saving users by state in /output ...")
    for state, users in listed_users_by_state.items():
        full_names = [f"{user[0]} {user[1]}" for user in users]
        Path(output_dir / f"{state}.txt").write_text('\n'.join(full_names))
    logger.debug("DONE.")


def main(args: List) -> None:
    """
    Main function to handle the program execution.
    
    Args:
        args (List): Command line arguments
    """
    parsed_args = parse_args(args)
    if parsed_args.v:
        logger.setLevel(logging.DEBUG)
        logger.debug("Verbose mode activated - Debug level enabled")
    if not parsed_args.c:
        count = 5
    else:
        count = parsed_args.c

    response = get_dataapi_data(parsed_args.username, parsed_args.password, count)
    listed_user_data = parse_response(response)

    listed_users_by_state = list_users_by_state(listed_user_data)
    output_dir = Path('output') / 'United States'
    output_dir.mkdir(parents=True, exist_ok=True)
    save_users_by_state(listed_users_by_state, output_dir)

if __name__ == '__main__':
    main(sys.argv[1:])