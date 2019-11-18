import random
import re
import json

def create_alphabet(numbers = True, upper_case = True, lower_case = True):

    alphabet = []
    # Numbers
    if numbers:
        for letter in range(65, 91):
            alphabet.append(chr(letter))

    # Upper Case
    if upper_case:
        for letter in range(65, 91):
            alphabet.append(chr(letter))

    # Lower Case
    if lower_case:
        for letter in range(65, 91):
            alphabet.append(chr(letter))

    return alphabet

def generate_token(length, word = ''):
    word = re.sub('[^A-Za-z0-9]', '', word)
    length = length - len(word)
    token = ''.join(random.choice(create_alphabet()) for _ in range(length))
    for char in word.upper():
        index = range(len(token))
        token = token[index:] + char + token[:index]
    return token

def list_to_json(value_list):
    return list(map(lambda value: value.json(), value_list))

