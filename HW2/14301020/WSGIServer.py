import socket
import sys
import io
import time
import os

SERVER_ADDRESS = (HOST, PORT) = '127.0.0.1', 8888


class WSGIServer(object):
    address_family = socket.AF_INET
    socket_type = socket.SOCK_STREAM
    request_queue_size = 10

    def __init__(self, server_address):
        # Create a listening socket
        self.listen_socket = listen_socket = socket.socket(
            self.address_family,
            self.socket_type
        )
        # Allow to reuse the same address
        # Tutorial -> http://www.cnblogs.com/xiaowuyi/archive/2012/08/06/2625509.html
        listen_socket.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
        # Bind
        listen_socket.bind(server_address)
        # Activate
        listen_socket.listen(self.request_queue_size)
        # Get server host name and port
        host, port = self.listen_socket.getsockname()[:2]
        self.server_name = socket.getfqdn(host)
        self.server_port = port
        # Return headers set by Web framework/Web application
        self.headers_set = []

    def set_app(self, application):
        self.application = application

    def serve_forever(self):
        listen_socket = self.listen_socket
        while True:
            # New client connection
            self.client_connection, client_address = listen_socket.accept()
            # Handle one request and close the client connection. Then
            # loop over to wait for another client connection
            self.handle_one_request()

    def handle_one_request(self):
        self.request_data = request_data = self.client_connection.recv(1024).decode('utf8')
        # Print formatted request data a la 'curl -v'
        print(''.join(
            '< {line}\n'.format(line=line)
            for line in request_data.splitlines()
        ))

        if request_data == '':
            self.client_connection.close()
            return

        self.parse_request(request_data)

        # Construct environment dictionary using request data
        env = self.get_environ()

        # It's time to call our application callable and get
        # back a result that will become HTTP response body
        result = self.application(env, self.start_response)

        # Construct a response and send it back to the client
        self.finish_response(result)

    def parse_request(self, text):
        request_line = text.splitlines()[0]
        # strip is useless since '\r\n' is not in request_line
        # request_line = request_line.rstrip('\r\n')
        # Break down the request line into components
        (self.request_method,  # GET
         self.path,  # /hello
         self.request_version  # HTTP/1.1
         ) = request_line.split()

    def get_environ(self):
        env = {}
        # The following code snippet does not follow PEP8 conventions
        # but it's formatted the way it is for demonstration purposes
        # to emphasize the required variables and their values
        #
        # Required WSGI variables
        env['wsgi.version'] = (1, 0)
        env['wsgi.url_scheme'] = 'http'
        env['wsgi.input'] = io.StringIO(self.request_data)
        env['wsgi.errors'] = sys.stderr
        env['wsgi.multithread'] = False
        env['wsgi.multiprocess'] = False
        env['wsgi.run_once'] = False
        # Required CGI variables
        env['REQUEST_METHOD'] = self.request_method  # GET
        env['PATH_INFO'] = self.path  # /hello
        env['SERVER_NAME'] = self.server_name  # localhost
        env['SERVER_PORT'] = str(self.server_port)  # 8888
        return env

    def start_response(self, status, response_headers, exc_info=None):
        # Add necessary server headers
        server_headers = [
            ('Date', time.strftime('%Y-%m-%d %H:%M:%S', time.localtime())),
            ('Server', 'WSGIServer Ver1.0'),
        ]
        self.headers_set = [status, response_headers + server_headers]
        # To adhere to WSGI specification the start_response must return
        # a 'write' callable. We simplicity's sake we'll ignore that detail
        # for now.
        # return self.finish_response

    def finish_response(self, result):
        try:
            status, response_headers = self.headers_set
            response = 'HTTP/1.1 {status}\r\n'.format(status=status)
            for header in response_headers:
                response += '{0}: {1}\r\n'.format(*header)
            response += '\r\n'
            for data in result:
                # response:str data:bytes
                # So use decode to convert bytes to str
                response += data.decode('utf8')
            # Print formatted response data a la 'curl -v'
            print(''.join(
                '> {line}\n'.format(line=line)
                for line in response.splitlines()
            ))
            # Need to send bytes to client
            # So convert str to bytes by encode
            self.client_connection.sendall(response.encode('utf8'))
        finally:
            self.client_connection.close()


def make_server(server_address, application):
    server = WSGIServer(server_address)
    server.set_app(application)
    return server


def app(environ, start_response):
    path_info = environ['PATH_INFO'][1:]
    if os.path.isfile(path_info):
        if path_info[-5:] == '.html':
            status = '200 OK'
            response_headers = [('Content-Type', 'text/html')]
            start_response(status, response_headers)
            f = open(path_info, 'r')
            return [f.read().encode('utf8')]
    else:
        if '.' in path_info:
            status = '404 Not Found'
            response_headers = [('Content-Type', 'text/plain')]
            start_response(status, response_headers)
            return ['404 Not Found\n'.encode('utf8')]
        else:
            status = '200 OK'
            response_headers = [('Content-Type', 'text/plain')]
            start_response(status, response_headers)
            return [('hello ' + path_info).encode('utf8')]


if __name__ == '__main__':
    httpd = make_server(SERVER_ADDRESS, app)
    print('WSGIServer: Serving HTTP on port {port} ...\n'.format(port=PORT))
    httpd.serve_forever()
