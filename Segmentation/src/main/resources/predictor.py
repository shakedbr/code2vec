from common import common
from extractor import Extractor

SHOW_TOP_CONTEXTS = 10
MAX_PATH_LENGTH = 8
MAX_PATH_WIDTH = 2
JAR_PATH = 'C:/code2vec/JavaExtractor/JPredict/target/JavaExtractor-0.0.1-SNAPSHOT.jar'


class Predictor:
    exit_keywords = ['exit', 'quit', 'q']

    def __init__(self, config, model, file_path):
        model.predict([])
        self.model = model
        self.config = config
        self.path_extractor = Extractor(config,
                                        jar_path=JAR_PATH,
                                        max_path_length=MAX_PATH_LENGTH,
                                        max_path_width=MAX_PATH_WIDTH)
        self.file_path = file_path

    def predict(self):
        print('Starting prediction...')
        try:
            predict_lines, hash_to_string_dict = self.path_extractor.extract_paths(self.file_path)
        except ValueError as e:
            print(e)
            return
        results = self.model.predict(predict_lines)
        prediction_results = common.parse_results(results, hash_to_string_dict, topk=SHOW_TOP_CONTEXTS)
        return prediction_results
