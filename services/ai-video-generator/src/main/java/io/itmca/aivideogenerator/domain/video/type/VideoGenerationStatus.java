package io.itmca.aivideogenerator.domain.video.type;

public enum VideoGenerationStatus {
  PENDING,     // 대기중
  IN_PROGRESS, // 진행중
  COMPLETED,   // 완료
  FAILED       // 실패
}