import joblib


def toxic_comment_predict(comment):
    with open("tfidf_vectorizer.pkl", "rb") as f:
        vectorizer = joblib.load(f)

    with open("toxic_comment_model.pkl", "rb") as f:
        model = joblib.load(f)

    vec = vectorizer.transform([comment])
    res = model.predict(vec)
    return res[0] == 1
