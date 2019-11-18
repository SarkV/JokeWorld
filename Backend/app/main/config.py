import os

# uncomment the line below for postgres database url from environment variable
# postgres_local_base = os.environ['DATABASE_URL']

class Config:
    SECRET_KEY = os.getenv('SECRET_KEY', 'jwt-secret-string')
    JWT_SECRET_KEY = os.getenv('SECRET_KEY', 'jwt-secret-string')
    UPLOAD_FOLDER = '/var/www/jokeworld/images'
    JWT_REFRESH_TOKEN_EXPIRES = False
    MONGOALCHEMY_DATABASE = 'jokeworld'
    MONGOALCHEMY_SAFE_SESSION = True
    MONGOALCHEMY_PORT = 27017
    MONGOALCHEMY_SERVER = "127.0.0.1"
    MAIL_SERVER = 'smtp.gmail.com'
    MAIL_PORT = 465
    MAIL_USERNAME = 'jokeworld.avtdev@gmail.com'
    MAIL_PASSWORD = '1804Santurtzi'
    MAIL_USE_TLS = False
    MAIL_USE_SSL = True

class DevelopmentConfig(Config):
    DEBUG = True
    ENV = ""


class ProductionConfig(Config):
    DEBUG = False
    ENV = "Production"


config_by_name = dict(
    dev=DevelopmentConfig,
    prod=ProductionConfig
)

key = Config.SECRET_KEY
