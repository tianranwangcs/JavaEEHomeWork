import socket
import threading

s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)

def work_thread(conn,addr):
    while(1):
        str = conn.recv(1024).decode('utf8')
        if (str[0:4] != "exit"):
            str = str[::-1]
            conn.send(str.encode('utf8'))
        else:
            print('Disconnect from', addr)
            break
    conn.close()


def main():
    HOST = '127.0.0.1'
    PORT = 3333
    s.bind((HOST, PORT))
    s.listen(10)
    while (1):
        conn, addr = s.accept()
        print('Connecting', addr)
        thread=threading.Thread(target=work_thread,args=(conn,addr))
        thread.start()

if __name__=='__main__':
    main()
