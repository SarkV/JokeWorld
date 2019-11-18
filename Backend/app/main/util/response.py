
import traceback
import logging

CODE = {
	200 : "OK",
	201 : "Created",
	202 : "Accepted",
	204 : "No Content",
	205 : "Reset Content",
	206 : "Partial Content",
	301 : "Moved Permanently",
	302 : "Found",
	303 : "See Other",
	304 : "Not Modified",
	307 : "Temporary Redirect",
	400 : "Bad Request",
	401 : "Unauthorized",
	402 : "Payment Required",
	403 : "Forbidden",
	404 : "Not Found",
	405 : "Method Not Allowed",
	406 : "Not Acceptable",
	408 : "Request Timeout",
	409 : "Already Exists",
	412 : "Precondition Failed",
	415 : "Unsupported Media Type",
	428 : "Precondition Required",
	500 : "Internal Server Error",
	501 : "Not Implemented"
}

class Response():
	def __init__(self, status, *args, **kwargs):
		self.status = status
		self.message = CODE[status]
		if kwargs:
			self.response = kwargs
		else:
			self.response = None

def catchException(e):
    if e.__class__.__name__ == "BadRequest":
        return Response(400).__dict__
    else:
        logging.error(traceback.format_exc())
        return Response(500).__dict__
