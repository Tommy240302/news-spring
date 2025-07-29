import joblib
import sys
import warnings

warnings.filterwarnings("ignore")

def toxic_comment_predict(comment):
    with open("toxic-comment-detect/tfidf_vectorizer.pkl", "rb") as f:
        vectorizer = joblib.load(f)

    with open("toxic-comment-detect/toxic_comment_model.pkl", "rb") as f:
        model = joblib.load(f)

    vec = vectorizer.transform([comment])
    res = model.predict(vec)
    return res[0] == 1

if __name__ == "__main__":
    if len(sys.argv) > 1:
        print(toxic_comment_predict(sys.argv[1]))
    else:
        print("No name provided.")
