package telegramium.bots

/** This object represents a video file.
  *
  * @param fileId
  *   Identifier for this file, which can be used to download or reuse the file
  * @param fileUniqueId
  *   Unique identifier for this file, which is supposed to be the same over time and for different bots. Can't be used
  *   to download or reuse the file.
  * @param width
  *   Video width as defined by the sender
  * @param height
  *   Video height as defined by the sender
  * @param duration
  *   Duration of the video in seconds as defined by the sender
  * @param thumbnail
  *   Optional. Video thumbnail
  * @param cover
  *   Optional. Available sizes of the cover of the video in the message
  * @param startTimestamp
  *   Optional. Timestamp in seconds from which the video will play in the message
  * @param fileName
  *   Optional. Original filename as defined by the sender
  * @param mimeType
  *   Optional. MIME type of the file as defined by the sender
  * @param fileSize
  *   Optional. File size in bytes. It can be bigger than 2&#94;31 and some programming languages may have
  *   difficulty/silent defects in interpreting it. But it has at most 52 significant bits, so a signed 64-bit integer
  *   or double-precision float type are safe for storing this value.
  */
final case class Video(
  fileId: String,
  fileUniqueId: String,
  width: Int,
  height: Int,
  duration: Int,
  thumbnail: Option[PhotoSize] = Option.empty,
  cover: List[PhotoSize] = List.empty,
  startTimestamp: Option[Int] = Option.empty,
  fileName: Option[String] = Option.empty,
  mimeType: Option[String] = Option.empty,
  fileSize: Option[Long] = Option.empty
)
