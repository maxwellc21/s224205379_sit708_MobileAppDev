from flask import Flask, request, jsonify
import requests
import json

app = Flask(__name__)

HUGGINGFACE_API_URL = "https://api-inference.huggingface.co/models/meta-llama/Llama-3-70b-instruct"
HUGGINGFACE_TOKEN = "Bearer YOUR_HF_API_KEY"  # Replace this securely later

headers = {
    "Authorization": HUGGINGFACE_TOKEN,
    "Content-Type": "application/json"
}

@app.route("/api/generate-quiz", methods=["POST"])
def generate_quiz():
    data = request.get_json()
    quiz_type = data.get("quizType", "mcq")
    topic = data.get("topic", "Android Development")

    prompt = f"""
    Generate 5 {quiz_type} Android quiz questions on '{topic}'.
    Each question should include:
    - questionType
    - question
    - options
    - correctAnswers (list)
    - hint
    - explanation
    Return only a valid JSON array.
    """

    res = requests.post(HUGGINGFACE_API_URL, headers=headers, json={"inputs": prompt})
    result = res.json()

    try:
        if isinstance(result, list) and "generated_text" in result[0]:
            return jsonify(json.loads(result[0]["generated_text"]))
        else:
            return jsonify({"error": "Invalid response", "raw": result}), 500
    except Exception as e:
        return jsonify({"error": str(e), "raw": result}), 500

@app.route("/")
def home():
    return "LLaMA 3 Quiz API is running!"

if __name__ == "__main__":
    app.run()
