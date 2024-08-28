package io.itmca.lifepuzzle.global.ai.stt;

import io.itmca.lifepuzzle.global.infra.file.CustomFile;

public interface SpeechToTextService {
  String transcribeAudio(CustomFile customFile);
}
