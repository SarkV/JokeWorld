import json
from json import JSONEncoder
from ..model.base_model import BaseModel


class JSONEncoder(JSONEncoder):
    def default (self, obj):
        if isinstance (obj, BaseModel):
            return obj.json()
        return json.JSONEncoder.default(self, obj)
