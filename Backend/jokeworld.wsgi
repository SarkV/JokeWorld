import sys
import logging
import os

logging.basicConfig(stream=sys.stderr)
sys.path.insert(0, '/home/jokeworld/flask/')

import monitor
monitor.start(interval=1.0)
monitor.track('/home/jokeworld/flask/')

from app import blueprint
from flask_script import Manager

from app.main import create_app

application = create_app(os.getenv('BOILERPLATE_ENV') or 'dev')

application.register_blueprint(blueprint)

application.app_context().push()

manager = Manager(application)

@manager.command
def run():
    application.run()

if __name__ == '__main__':
    manager.run()

