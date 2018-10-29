from argparse import ArgumentParser
import sys
import json
from py4j.java_gateway import JavaGateway, CallbackServerParameters
from common import Config, VocabType
from model import Model
from predictor import Predictor

INPUT_FILE = "C:/code2vec/python_input.java"

class PythonListener():

    def __init__(self, gateway):
        self.gateway = gateway
        self.model = None
        self.config = None
        self.predictor = None

    def set_params(self, config, model, predictor):
        self.model = model
        self.config = config
        self.predictor = predictor

    def runMethod(self, s):
        save_to_file(INPUT_FILE, s)
        print(s)
        res = self.predictor.predict()[0]
        return json.dumps(res.__dict__)

    class Java:
        implements = ["utils.IPythonServerEntryPoint"]

def save_to_file(path, text):
    with open(path, "w") as f:
        f.write(text)


def init_model():
    parser = ArgumentParser()
    parser.add_argument("-d", "--data", dest="data_path",
                        help="path to preprocessed dataset", required=False)
    parser.add_argument("-te", "--test", dest="test_path",
                        help="path to test file", metavar="FILE", required=False)

    is_training = '--train' in sys.argv or '-tr' in sys.argv
    parser.add_argument("-s", "--save", dest="save_path",
                        help="path to save file", metavar="FILE", required=False)
    parser.add_argument("-w2v", "--save_word2v", dest="save_w2v",
                        help="path to save file", metavar="FILE", required=False)
    parser.add_argument("-t2v", "--save_target2v", dest="save_t2v",
                        help="path to save file", metavar="FILE", required=False)
    parser.add_argument("-l", "--load", dest="load_path",
                        help="path to save file", metavar="FILE", required=False, default="C:/code2vec/models/java14m/models/java14_model/saved_model_iter8.release")
    parser.add_argument('--save_w2v', dest='save_w2v', required=False,
                        help="save word (token) vectors in word2vec format")
    parser.add_argument('--save_t2v', dest='save_t2v', required=False,
                        help="save target vectors in word2vec format")
    parser.add_argument('--release', action='store_true',
                        help='if specified and loading a trained model, release the loaded model for a lower model '
                             'size.')
    parser.add_argument('--predict', action='store_true', default=True)
    args = parser.parse_args()

    config = Config.get_default_config(args)
    model = Model(config)
    predictor = Predictor(config, model, INPUT_FILE)
    gateway = JavaGateway(callback_server_parameters=CallbackServerParameters())
    listener = PythonListener(gateway)
    listener.set_params(config, model, predictor)
    return listener


if __name__ == "__main__":
    print("[P]: Python server started")
    listener = init_model()
    listener.gateway.entry_point.register(listener)
    save_to_file(INPUT_FILE, "")
    print("[P]: Python server ready to predict")
    while True:
        pass



