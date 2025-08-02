from toxic_predict import toxic_comment_predict
from fastapi import FastAPI
from fastapi.middleware.cors import CORSMiddleware
from pydantic import BaseModel

app = FastAPI()

origins = [
    "http://loalhost:5173",
    "http://127.0.0.1:5273",
    "http://127.0.0.1:6969"
]

app.add_middleware(
    CORSMiddleware,
    allow_origins=origins,
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)


class CommentRequest(BaseModel):
    comment: str


@app.post("/toxic-comment-detect")
def commentDetect(req: CommentRequest):
    result = toxic_comment_predict(req.comment)
    return {"is_toxic": bool(result)}
