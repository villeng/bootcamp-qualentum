# Script - write names into files
The functionality of this script is to make a request to https://random-data-api.com/ and fetch N users. Then, the name 
and surname of each user is printed into a file whose name is the name of the state the user lives in.

The directory will be:

```output/United States/Minessota.txt```

And the content of the file:
```
Jane Doe
Michael Smith
```

## Use

The script has to be executed with:

```python retrieve_names.py [options] ```

## Installation

```bash
pip install -r requirements.txt
```

## Use

### Basic syntax

```bash
python script.py [opciones]
```

### Optional arguments

| Argument     | Description                                          | Default | Required |
|--------------|------------------------------------------------------|---------|----------|
| `-c, --count` | Specifies the number of instances (users) to retrieve | 5       | No       |
| `-v, --verbose` | Enables debug logging                                | False   | No       |


