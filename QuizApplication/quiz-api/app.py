from flask import Flask, request, jsonify
import requests
import json
import os
import re

app = Flask(__name__)

# Hugging Face LLaMA 3 API
HUGGINGFACE_API_URL = "https://api-inference.huggingface.co/models/meta-llama/Meta-Llama-3-8B-Instruct"
HUGGINGFACE_TOKEN = os.environ.get("HUGGINGFACE_TOKEN")

headers = {
    "Authorization": f"Bearer {HUGGINGFACE_TOKEN}",
    "Content-Type": "application/json"
}

@app.route("/")
def home():
    return "✅ LLaMA 3 API for Multiple Choice Quiz is LIVE!"

@app.route("/api/generate-quiz", methods=["POST"])
def generate_quiz():
    try:
        data = request.get_json()
        topic = data.get("topic", "General Knowledge")
        difficulty = data.get("difficulty", "Easy")

        prompt = f"""
        You are a JSON-only quiz generator API.

        Return exactly 11 multiple choice questions for the topic \"{topic}\" at \"{difficulty}\" difficulty.

        Respond ONLY with a valid JSON array of 11 objects. No introductions. No extra words. No markdown. No formatting.

        Each object in the array must follow this format:
        {{
          "questionType": "multipleChoice",
          "question": "string",
          "options": ["Option A", "Option B", "Option C", "Option D"],
          "correctAnswers": [integer index of correct option, from 0 to 3],
          "hint": "string",
          "explanation": "string"
        }}

        Do NOT return anything else. Only return a pure JSON array.
        """

        response = requests.post(
            HUGGINGFACE_API_URL,
            headers=headers,
            json={"inputs": prompt},
            timeout=120
        )

        if response.status_code != 200:
            return jsonify({"error": f"Request failed with status {response.status_code}"}), 500
        
        result = response.json()

        if isinstance(result, list) and "generated_text" in result[0]:
            raw = result[0]["generated_text"]
            
            json_start = raw.find("[")
            if json_start != -1:
                json_data = raw[json_start:]
                match = re.search(r'\[\s*{.*?}\s*\]', json_data, re.DOTALL)
                if match:
                    quiz_data = json.loads(match.group(0))

                    for q in quiz_data:
                        if not all(isinstance(ans, int) for ans in q.get("correctAnswers", [])):
                            return jsonify({"error": "Invalid 'correctAnswers' format: must use integer index not letter."}), 400

                    return jsonify(quiz_data)

        return jsonify({"error": "LLaMA response could not be parsed", "raw": result}), 500

    except json.JSONDecodeError as e:
        return jsonify({"error": f"JSON Decode Error: {str(e)}"}), 500
    except requests.exceptions.Timeout:
        return jsonify({"error": "Request to Hugging Face timed out"}), 504
    except requests.exceptions.ConnectionError:
        return jsonify({"error": "Network connection error"}), 500
    except Exception as e:
        return jsonify({"error": str(e)}), 500

if __name__ == "__main__":
    port = int(os.environ.get("PORT", 5000))
    app.run(host="0.0.0.0", port=port)
