import uuid
from datetime import datetime, timedelta

from .. import db, mail
from ..model.config import Config

def getConfig(dateChange = None):
    if dateChange:
        return Config.query.filter({"updateDate" : {"$gte": dateChange}}).all()
    else:
        return Config.query.filter().all()
