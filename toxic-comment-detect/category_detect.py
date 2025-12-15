from bs4 import BeautifulSoup
from pyvi import ViTokenizer
import gensim
import pickle

category_map = {
    'Chinh tri Xa hoi': 'Chính trị - Xã hội',
    'Doi song': 'Đời sống',
    'Khoa hoc': 'Khoa học',
    'Kinh doanh': 'Kinh doanh',
    'Phap luat': 'Pháp luật',
    'Suc khoe': 'Sức khỏe',
    'The gioi': 'Thế giới',
    'The thao': 'Thể thao',
    'Van hoa': 'Văn hóa',
    'Vi tinh': 'Vi tính'
}

def category_detect(content):
    tfidf_vect = pickle.load(open("tfidf_vect.pkl", "rb"))
    classifier = pickle.load(open("logistic_model.pkl", "rb"))

    soup = BeautifulSoup(content, "html.parser")
    text = soup.get_text(separator=" ", strip=True)
    pre_text = gensim.utils.simple_preprocess(text)
    pre_text = ' '.join(pre_text)
    pre_text = ViTokenizer.tokenize(pre_text)

    X = tfidf_vect.transform([pre_text])

    y_pred = classifier.predict(X)

    return category_map.get(y_pred[0])