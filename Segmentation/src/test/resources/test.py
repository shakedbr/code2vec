from py4j.java_gateway import JavaGateway, CallbackServerParameters


class PythonListenerTest():

    def __init__(self, gateway):
        self.gateway = gateway

    def runMethod(self, s):
        return "Python server return value"

    class Java:
        implements = ["utils.IPythonServerEntryPoint"]

if __name__ == "__main__":
    print("Python server started")
    gateway = JavaGateway(
        callback_server_parameters=CallbackServerParameters())
    listener = PythonListenerTest(gateway)
    gateway.entry_point.register(listener)

