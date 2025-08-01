package telegramium.bots.client

import io.circe.syntax._
import CirceImplicits._
import telegramium.bots._
import telegramium.bots.CirceImplicits._
import telegramium.bots.SentWebAppMessage
import telegramium.bots.MessageId
import telegramium.bots.ChatInviteLink
import telegramium.bots.ForumTopic
import telegramium.bots.Message
import telegramium.bots.Story
import telegramium.bots.Gifts
import telegramium.bots.OwnedGifts
import telegramium.bots.StarAmount
import telegramium.bots.BusinessConnection
import telegramium.bots.ChatFullInfo
import telegramium.bots.ChatMember
import telegramium.bots.MenuButton
import telegramium.bots.Sticker
import telegramium.bots.File
import telegramium.bots.GameHighScore
import telegramium.bots.User
import telegramium.bots.BotCommand
import telegramium.bots.ChatAdministratorRights
import telegramium.bots.BotDescription
import telegramium.bots.BotName
import telegramium.bots.BotShortDescription
import telegramium.bots.StarTransactions
import telegramium.bots.StickerSet
import telegramium.bots.Update
import telegramium.bots.UserChatBoosts
import telegramium.bots.UserProfilePhotos
import telegramium.bots.WebhookInfo
import telegramium.bots.PreparedInlineMessage
import telegramium.bots.Poll

trait Methods {

  import io.circe.Decoder

  private implicit def decodeEither[A, B](implicit
    decoderA: Decoder[A],
    decoderB: Decoder[B]
  ): Decoder[Either[A, B]] = decoderA.either(decoderB)

  /** Use this method to add a new sticker to a set created by the bot. Emoji sticker sets can have up to 200 stickers.
    * Other sticker sets can have up to 120 stickers. Returns True on success.
    *
    * @param userId
    *   User identifier of sticker set owner
    * @param name
    *   Sticker set name
    * @param sticker
    *   A JSON-serialized object with information about the added sticker. If exactly the same sticker had already been
    *   added to the set, then the set isn't changed.
    */
  def addStickerToSet(userId: Long, name: String, sticker: InputSticker): Method[Boolean] = {
    val req = AddStickerToSetReq(userId, name, sticker)
    MethodReq[Boolean]("addStickerToSet", req.asJson)
  }

  /** Use this method to send answers to callback queries sent from inline keyboards. The answer will be displayed to
    * the user as a notification at the top of the chat screen or as an alert. On success, True is returned.
    *
    * @param callbackQueryId
    *   Unique identifier for the query to be answered
    * @param text
    *   Text of the notification. If not specified, nothing will be shown to the user, 0-200 characters
    * @param showAlert
    *   If True, an alert will be shown by the client instead of a notification at the top of the chat screen. Defaults
    *   to false.
    * @param url
    *   URL that will be opened by the user's client. If you have created a Game and accepted the conditions via
    *   &#064;BotFather, specify the URL that opens your game - note that this will only work if the query comes from a
    *   callback_game button. Otherwise, you may use links like t.me/your_bot?start=XXXX that open your bot with a
    *   parameter.
    * @param cacheTime
    *   The maximum amount of time in seconds that the result of the callback query may be cached client-side. Telegram
    *   apps will support caching starting in version 3.14. Defaults to 0.
    */
  def answerCallbackQuery(
    callbackQueryId: String,
    text: Option[String] = Option.empty,
    showAlert: Option[Boolean] = Option.empty,
    url: Option[String] = Option.empty,
    cacheTime: Option[Int] = Option.empty
  ): Method[Boolean] = {
    val req = AnswerCallbackQueryReq(callbackQueryId, text, showAlert, url, cacheTime)
    MethodReq[Boolean]("answerCallbackQuery", req.asJson)
  }

  /** Use this method to send answers to an inline query. On success, True is returned. No more than 50 results per
    * query are allowed.
    *
    * @param inlineQueryId
    *   Unique identifier for the answered query
    * @param results
    *   A JSON-serialized array of results for the inline query
    * @param cacheTime
    *   The maximum amount of time in seconds that the result of the inline query may be cached on the server. Defaults
    *   to 300.
    * @param isPersonal
    *   Pass True if results may be cached on the server side only for the user that sent the query. By default, results
    *   may be returned to any user who sends the same query.
    * @param nextOffset
    *   Pass the offset that a client should send in the next query with the same text to receive more results. Pass an
    *   empty string if there are no more results or if you don't support pagination. Offset length can't exceed 64
    *   bytes.
    * @param button
    *   A JSON-serialized object describing a button to be shown above inline query results
    */
  def answerInlineQuery(
    inlineQueryId: String,
    results: List[InlineQueryResult] = List.empty,
    cacheTime: Option[Int] = Option.empty,
    isPersonal: Option[Boolean] = Option.empty,
    nextOffset: Option[String] = Option.empty,
    button: Option[InlineQueryResultsButton] = Option.empty
  ): Method[Boolean] = {
    val req = AnswerInlineQueryReq(inlineQueryId, results, cacheTime, isPersonal, nextOffset, button)
    MethodReq[Boolean]("answerInlineQuery", req.asJson)
  }

  /** Once the user has confirmed their payment and shipping details, the Bot API sends the final confirmation in the
    * form of an Update with the field pre_checkout_query. Use this method to respond to such pre-checkout queries. On
    * success, True is returned. Note: The Bot API must receive an answer within 10 seconds after the pre-checkout query
    * was sent.
    *
    * @param preCheckoutQueryId
    *   Unique identifier for the query to be answered
    * @param ok
    *   Specify True if everything is alright (goods are available, etc.) and the bot is ready to proceed with the
    *   order. Use False if there are any problems.
    * @param errorMessage
    *   Required if ok is False. Error message in human readable form that explains the reason for failure to proceed
    *   with the checkout (e.g. "Sorry, somebody just bought the last of our amazing black T-shirts while you were busy
    *   filling out your payment details. Please choose a different color or garment!"). Telegram will display this
    *   message to the user.
    */
  def answerPreCheckoutQuery(
    preCheckoutQueryId: String,
    ok: Boolean,
    errorMessage: Option[String] = Option.empty
  ): Method[Boolean] = {
    val req = AnswerPreCheckoutQueryReq(preCheckoutQueryId, ok, errorMessage)
    MethodReq[Boolean]("answerPreCheckoutQuery", req.asJson)
  }

  /** If you sent an invoice requesting a shipping address and the parameter is_flexible was specified, the Bot API will
    * send an Update with a shipping_query field to the bot. Use this method to reply to shipping queries. On success,
    * True is returned.
    *
    * @param shippingQueryId
    *   Unique identifier for the query to be answered
    * @param ok
    *   Pass True if delivery to the specified address is possible and False if there are any problems (for example, if
    *   delivery to the specified address is not possible)
    * @param shippingOptions
    *   Required if ok is True. A JSON-serialized array of available shipping options.
    * @param errorMessage
    *   Required if ok is False. Error message in human readable form that explains why it is impossible to complete the
    *   order (e.g. “Sorry, delivery to your desired address is unavailable”). Telegram will display this message to the
    *   user.
    */
  def answerShippingQuery(
    shippingQueryId: String,
    ok: Boolean,
    shippingOptions: List[ShippingOption] = List.empty,
    errorMessage: Option[String] = Option.empty
  ): Method[Boolean] = {
    val req = AnswerShippingQueryReq(shippingQueryId, ok, shippingOptions, errorMessage)
    MethodReq[Boolean]("answerShippingQuery", req.asJson)
  }

  /** Use this method to set the result of an interaction with a Web App and send a corresponding message on behalf of
    * the user to the chat from which the query originated. On success, a SentWebAppMessage object is returned.
    *
    * @param webAppQueryId
    *   Unique identifier for the query to be answered
    * @param result
    *   A JSON-serialized object describing the message to be sent
    */
  def answerWebAppQuery(webAppQueryId: String, result: InlineQueryResult): Method[SentWebAppMessage] = {
    val req = AnswerWebAppQueryReq(webAppQueryId, result)
    MethodReq[SentWebAppMessage]("answerWebAppQuery", req.asJson)
  }

  /** Use this method to approve a chat join request. The bot must be an administrator in the chat for this to work and
    * must have the can_invite_users administrator right. Returns True on success.
    *
    * @param chatId
    *   Unique identifier for the target chat or username of the target channel (in the format &#064;channelusername)
    * @param userId
    *   Unique identifier of the target user
    */
  def approveChatJoinRequest(chatId: ChatId, userId: Long): Method[Boolean] = {
    val req = ApproveChatJoinRequestReq(chatId, userId)
    MethodReq[Boolean]("approveChatJoinRequest", req.asJson)
  }

  /** Use this method to ban a user in a group, a supergroup or a channel. In the case of supergroups and channels, the
    * user will not be able to return to the chat on their own using invite links, etc., unless unbanned first. The bot
    * must be an administrator in the chat for this to work and must have the appropriate administrator rights. Returns
    * True on success.
    *
    * @param chatId
    *   Unique identifier for the target group or username of the target supergroup or channel (in the format
    *   &#064;channelusername)
    * @param userId
    *   Unique identifier of the target user
    * @param untilDate
    *   Date when the user will be unbanned; Unix time. If user is banned for more than 366 days or less than 30 seconds
    *   from the current time they are considered to be banned forever. Applied for supergroups and channels only.
    * @param revokeMessages
    *   Pass True to delete all messages from the chat for the user that is being removed. If False, the user will be
    *   able to see messages in the group that were sent before the user was removed. Always True for supergroups and
    *   channels.
    */
  def banChatMember(
    chatId: ChatId,
    userId: Long,
    untilDate: Option[Int] = Option.empty,
    revokeMessages: Option[Boolean] = Option.empty
  ): Method[Boolean] = {
    val req = BanChatMemberReq(chatId, userId, untilDate, revokeMessages)
    MethodReq[Boolean]("banChatMember", req.asJson)
  }

  /** Use this method to ban a channel chat in a supergroup or a channel. Until the chat is unbanned, the owner of the
    * banned chat won't be able to send messages on behalf of any of their channels. The bot must be an administrator in
    * the supergroup or channel for this to work and must have the appropriate administrator rights. Returns True on
    * success.
    *
    * @param chatId
    *   Unique identifier for the target chat or username of the target channel (in the format &#064;channelusername)
    * @param senderChatId
    *   Unique identifier of the target sender chat
    */
  def banChatSenderChat(chatId: ChatId, senderChatId: Long): Method[Boolean] = {
    val req = BanChatSenderChatReq(chatId, senderChatId)
    MethodReq[Boolean]("banChatSenderChat", req.asJson)
  }

  /** Use this method to close the bot instance before moving it from one local server to another. You need to delete
    * the webhook before calling this method to ensure that the bot isn't launched again after server restart. The
    * method will return error 429 in the first 10 minutes after the bot is launched. Returns True on success. Requires
    * no parameters.
    */
  def close(): Method[Boolean] = {
    val req = CloseReq
    MethodReq[Boolean]("close", req.asJson)
  }

  /** Use this method to close an open topic in a forum supergroup chat. The bot must be an administrator in the chat
    * for this to work and must have the can_manage_topics administrator rights, unless it is the creator of the topic.
    * Returns True on success.
    *
    * @param chatId
    *   Unique identifier for the target chat or username of the target supergroup (in the format
    *   &#064;supergroupusername)
    * @param messageThreadId
    *   Unique identifier for the target message thread of the forum topic
    */
  def closeForumTopic(chatId: ChatId, messageThreadId: Int): Method[Boolean] = {
    val req = CloseForumTopicReq(chatId, messageThreadId)
    MethodReq[Boolean]("closeForumTopic", req.asJson)
  }

  /** Use this method to close an open 'General' topic in a forum supergroup chat. The bot must be an administrator in
    * the chat for this to work and must have the can_manage_topics administrator rights. Returns True on success.
    *
    * @param chatId
    *   Unique identifier for the target chat or username of the target supergroup (in the format
    *   &#064;supergroupusername)
    */
  def closeGeneralForumTopic(chatId: ChatId): Method[Boolean] = {
    val req = CloseGeneralForumTopicReq(chatId)
    MethodReq[Boolean]("closeGeneralForumTopic", req.asJson)
  }

  /** Converts a given regular gift to Telegram Stars. Requires the can_convert_gifts_to_stars business bot right.
    * Returns True on success.
    *
    * @param businessConnectionId
    *   Unique identifier of the business connection
    * @param ownedGiftId
    *   Unique identifier of the regular gift that should be converted to Telegram Stars
    */
  def convertGiftToStars(businessConnectionId: String, ownedGiftId: String): Method[Boolean] = {
    val req = ConvertGiftToStarsReq(businessConnectionId, ownedGiftId)
    MethodReq[Boolean]("convertGiftToStars", req.asJson)
  }

  /** Use this method to copy messages of any kind. Service messages, paid media messages, giveaway messages, giveaway
    * winners messages, and invoice messages can't be copied. A quiz poll can be copied only if the value of the field
    * correct_option_id is known to the bot. The method is analogous to the method forwardMessage, but the copied
    * message doesn't have a link to the original message. Returns the MessageId of the sent message on success.
    *
    * @param chatId
    *   Unique identifier for the target chat or username of the target channel (in the format &#064;channelusername)
    * @param fromChatId
    *   Unique identifier for the chat where the original message was sent (or channel username in the format
    *   &#064;channelusername)
    * @param messageId
    *   Message identifier in the chat specified in from_chat_id
    * @param messageThreadId
    *   Unique identifier for the target message thread (topic) of the forum; for forum supergroups only
    * @param videoStartTimestamp
    *   New start timestamp for the copied video in the message
    * @param caption
    *   New caption for media, 0-1024 characters after entities parsing. If not specified, the original caption is kept
    * @param parseMode
    *   Mode for parsing entities in the new caption. See formatting options for more details.
    * @param captionEntities
    *   A JSON-serialized list of special entities that appear in the new caption, which can be specified instead of
    *   parse_mode
    * @param showCaptionAboveMedia
    *   Pass True, if the caption must be shown above the message media. Ignored if a new caption isn't specified.
    * @param disableNotification
    *   Sends the message silently. Users will receive a notification with no sound.
    * @param protectContent
    *   Protects the contents of the sent message from forwarding and saving
    * @param allowPaidBroadcast
    *   Pass True to allow up to 1000 messages per second, ignoring broadcasting limits for a fee of 0.1 Telegram Stars
    *   per message. The relevant Stars will be withdrawn from the bot's balance
    * @param replyParameters
    *   Description of the message to reply to
    * @param replyMarkup
    *   Additional interface options. A JSON-serialized object for an inline keyboard, custom reply keyboard,
    *   instructions to remove a reply keyboard or to force a reply from the user
    */
  def copyMessage(
    chatId: ChatId,
    fromChatId: ChatId,
    messageId: Int,
    messageThreadId: Option[Int] = Option.empty,
    videoStartTimestamp: Option[Int] = Option.empty,
    caption: Option[String] = Option.empty,
    parseMode: Option[ParseMode] = Option.empty,
    captionEntities: List[MessageEntity] = List.empty,
    showCaptionAboveMedia: Option[Boolean] = Option.empty,
    disableNotification: Option[Boolean] = Option.empty,
    protectContent: Option[Boolean] = Option.empty,
    allowPaidBroadcast: Option[Boolean] = Option.empty,
    replyParameters: Option[ReplyParameters] = Option.empty,
    replyMarkup: Option[KeyboardMarkup] = Option.empty
  ): Method[MessageId] = {
    val req = CopyMessageReq(
      chatId,
      fromChatId,
      messageId,
      messageThreadId,
      videoStartTimestamp,
      caption,
      parseMode,
      captionEntities,
      showCaptionAboveMedia,
      disableNotification,
      protectContent,
      allowPaidBroadcast,
      replyParameters,
      replyMarkup
    )
    MethodReq[MessageId]("copyMessage", req.asJson)
  }

  /** Use this method to copy messages of any kind. If some of the specified messages can't be found or copied, they are
    * skipped. Service messages, paid media messages, giveaway messages, giveaway winners messages, and invoice messages
    * can't be copied. A quiz poll can be copied only if the value of the field correct_option_id is known to the bot.
    * The method is analogous to the method forwardMessages, but the copied messages don't have a link to the original
    * message. Album grouping is kept for copied messages. On success, an array of MessageId of the sent messages is
    * returned.
    *
    * @param chatId
    *   Unique identifier for the target chat or username of the target channel (in the format &#064;channelusername)
    * @param fromChatId
    *   Unique identifier for the chat where the original messages were sent (or channel username in the format
    *   &#064;channelusername)
    * @param messageThreadId
    *   Unique identifier for the target message thread (topic) of the forum; for forum supergroups only
    * @param messageIds
    *   A JSON-serialized list of 1-100 identifiers of messages in the chat from_chat_id to copy. The identifiers must
    *   be specified in a strictly increasing order.
    * @param disableNotification
    *   Sends the messages silently. Users will receive a notification with no sound.
    * @param protectContent
    *   Protects the contents of the sent messages from forwarding and saving
    * @param removeCaption
    *   Pass True to copy the messages without their captions
    */
  def copyMessages(
    chatId: ChatId,
    fromChatId: ChatId,
    messageThreadId: Option[Int] = Option.empty,
    messageIds: List[Int] = List.empty,
    disableNotification: Option[Boolean] = Option.empty,
    protectContent: Option[Boolean] = Option.empty,
    removeCaption: Option[Boolean] = Option.empty
  ): Method[List[MessageId]] = {
    val req = CopyMessagesReq(
      chatId,
      fromChatId,
      messageThreadId,
      messageIds,
      disableNotification,
      protectContent,
      removeCaption
    )
    MethodReq[List[MessageId]]("copyMessages", req.asJson)
  }

  /** Use this method to create an additional invite link for a chat. The bot must be an administrator in the chat for
    * this to work and must have the appropriate administrator rights. The link can be revoked using the method
    * revokeChatInviteLink. Returns the new invite link as ChatInviteLink object.
    *
    * @param chatId
    *   Unique identifier for the target chat or username of the target channel (in the format &#064;channelusername)
    * @param name
    *   Invite link name; 0-32 characters
    * @param expireDate
    *   Point in time (Unix timestamp) when the link will expire
    * @param memberLimit
    *   The maximum number of users that can be members of the chat simultaneously after joining the chat via this
    *   invite link; 1-99999
    * @param createsJoinRequest
    *   True, if users joining the chat via the link need to be approved by chat administrators. If True, member_limit
    *   can't be specified
    */
  def createChatInviteLink(
    chatId: ChatId,
    name: Option[String] = Option.empty,
    expireDate: Option[Int] = Option.empty,
    memberLimit: Option[Int] = Option.empty,
    createsJoinRequest: Option[Boolean] = Option.empty
  ): Method[ChatInviteLink] = {
    val req = CreateChatInviteLinkReq(chatId, name, expireDate, memberLimit, createsJoinRequest)
    MethodReq[ChatInviteLink]("createChatInviteLink", req.asJson)
  }

  /** Use this method to create a subscription invite link for a channel chat. The bot must have the can_invite_users
    * administrator rights. The link can be edited using the method editChatSubscriptionInviteLink or revoked using the
    * method revokeChatInviteLink. Returns the new invite link as a ChatInviteLink object.
    *
    * @param chatId
    *   Unique identifier for the target channel chat or username of the target channel (in the format
    *   &#064;channelusername)
    * @param subscriptionPeriod
    *   The number of seconds the subscription will be active for before the next payment. Currently, it must always be
    *   2592000 (30 days).
    * @param subscriptionPrice
    *   The amount of Telegram Stars a user must pay initially and after each subsequent subscription period to be a
    *   member of the chat; 1-10000
    * @param name
    *   Invite link name; 0-32 characters
    */
  def createChatSubscriptionInviteLink(
    chatId: ChatId,
    subscriptionPeriod: Int,
    subscriptionPrice: Int,
    name: Option[String] = Option.empty
  ): Method[ChatInviteLink] = {
    val req = CreateChatSubscriptionInviteLinkReq(chatId, subscriptionPeriod, subscriptionPrice, name)
    MethodReq[ChatInviteLink]("createChatSubscriptionInviteLink", req.asJson)
  }

  /** Use this method to create a topic in a forum supergroup chat. The bot must be an administrator in the chat for
    * this to work and must have the can_manage_topics administrator rights. Returns information about the created topic
    * as a ForumTopic object.
    *
    * @param chatId
    *   Unique identifier for the target chat or username of the target supergroup (in the format
    *   &#064;supergroupusername)
    * @param name
    *   Topic name, 1-128 characters
    * @param iconColor
    *   Color of the topic icon in RGB format. Currently, must be one of 7322096 (0x6FB9F0), 16766590 (0xFFD67E),
    *   13338331 (0xCB86DB), 9367192 (0x8EEE98), 16749490 (0xFF93B2), or 16478047 (0xFB6F5F)
    * @param iconCustomEmojiId
    *   Unique identifier of the custom emoji shown as the topic icon. Use getForumTopicIconStickers to get all allowed
    *   custom emoji identifiers.
    */
  def createForumTopic(
    chatId: ChatId,
    name: String,
    iconColor: Option[Int] = Option.empty,
    iconCustomEmojiId: Option[String] = Option.empty
  ): Method[ForumTopic] = {
    val req = CreateForumTopicReq(chatId, name, iconColor, iconCustomEmojiId)
    MethodReq[ForumTopic]("createForumTopic", req.asJson)
  }

  /** Use this method to create a link for an invoice. Returns the created invoice link as String on success.
    *
    * @param title
    *   Product name, 1-32 characters
    * @param description
    *   Product description, 1-255 characters
    * @param payload
    *   Bot-defined invoice payload, 1-128 bytes. This will not be displayed to the user, use it for your internal
    *   processes.
    * @param currency
    *   Three-letter ISO 4217 currency code, see more on currencies. Pass “XTR” for payments in Telegram Stars.
    * @param businessConnectionId
    *   Unique identifier of the business connection on behalf of which the link will be created. For payments in
    *   Telegram Stars only.
    * @param providerToken
    *   Payment provider token, obtained via &#064;BotFather. Pass an empty string for payments in Telegram Stars.
    * @param prices
    *   Price breakdown, a JSON-serialized list of components (e.g. product price, tax, discount, delivery cost,
    *   delivery tax, bonus, etc.). Must contain exactly one item for payments in Telegram Stars.
    * @param subscriptionPeriod
    *   The number of seconds the subscription will be active for before the next payment. The currency must be set to
    *   “XTR” (Telegram Stars) if the parameter is used. Currently, it must always be 2592000 (30 days) if specified.
    *   Any number of subscriptions can be active for a given bot at the same time, including multiple concurrent
    *   subscriptions from the same user. Subscription price must no exceed 10000 Telegram Stars.
    * @param maxTipAmount
    *   The maximum accepted amount for tips in the smallest units of the currency (integer, not float/double). For
    *   example, for a maximum tip of US$ 1.45 pass max_tip_amount = 145. See the exp parameter in currencies.json, it
    *   shows the number of digits past the decimal point for each currency (2 for the majority of currencies). Defaults
    *   to 0. Not supported for payments in Telegram Stars.
    * @param suggestedTipAmounts
    *   A JSON-serialized array of suggested amounts of tips in the smallest units of the currency (integer, not
    *   float/double). At most 4 suggested tip amounts can be specified. The suggested tip amounts must be positive,
    *   passed in a strictly increased order and must not exceed max_tip_amount.
    * @param providerData
    *   JSON-serialized data about the invoice, which will be shared with the payment provider. A detailed description
    *   of required fields should be provided by the payment provider.
    * @param photoUrl
    *   URL of the product photo for the invoice. Can be a photo of the goods or a marketing image for a service.
    * @param photoSize
    *   Photo size in bytes
    * @param photoWidth
    *   Photo width
    * @param photoHeight
    *   Photo height
    * @param needName
    *   Pass True if you require the user's full name to complete the order. Ignored for payments in Telegram Stars.
    * @param needPhoneNumber
    *   Pass True if you require the user's phone number to complete the order. Ignored for payments in Telegram Stars.
    * @param needEmail
    *   Pass True if you require the user's email address to complete the order. Ignored for payments in Telegram Stars.
    * @param needShippingAddress
    *   Pass True if you require the user's shipping address to complete the order. Ignored for payments in Telegram
    *   Stars.
    * @param sendPhoneNumberToProvider
    *   Pass True if the user's phone number should be sent to the provider. Ignored for payments in Telegram Stars.
    * @param sendEmailToProvider
    *   Pass True if the user's email address should be sent to the provider. Ignored for payments in Telegram Stars.
    * @param isFlexible
    *   Pass True if the final price depends on the shipping method. Ignored for payments in Telegram Stars.
    */
  def createInvoiceLink(
    title: String,
    description: String,
    payload: String,
    currency: String,
    businessConnectionId: Option[String] = Option.empty,
    providerToken: Option[String] = Option.empty,
    prices: List[LabeledPrice] = List.empty,
    subscriptionPeriod: Option[Int] = Option.empty,
    maxTipAmount: Option[Int] = Option.empty,
    suggestedTipAmounts: List[Int] = List.empty,
    providerData: Option[String] = Option.empty,
    photoUrl: Option[String] = Option.empty,
    photoSize: Option[Long] = Option.empty,
    photoWidth: Option[Int] = Option.empty,
    photoHeight: Option[Int] = Option.empty,
    needName: Option[Boolean] = Option.empty,
    needPhoneNumber: Option[Boolean] = Option.empty,
    needEmail: Option[Boolean] = Option.empty,
    needShippingAddress: Option[Boolean] = Option.empty,
    sendPhoneNumberToProvider: Option[Boolean] = Option.empty,
    sendEmailToProvider: Option[Boolean] = Option.empty,
    isFlexible: Option[Boolean] = Option.empty
  ): Method[String] = {
    val req = CreateInvoiceLinkReq(
      title,
      description,
      payload,
      currency,
      businessConnectionId,
      providerToken,
      prices,
      subscriptionPeriod,
      maxTipAmount,
      suggestedTipAmounts,
      providerData,
      photoUrl,
      photoSize,
      photoWidth,
      photoHeight,
      needName,
      needPhoneNumber,
      needEmail,
      needShippingAddress,
      sendPhoneNumberToProvider,
      sendEmailToProvider,
      isFlexible
    )
    MethodReq[String]("createInvoiceLink", req.asJson)
  }

  /** Use this method to create a new sticker set owned by a user. The bot will be able to edit the sticker set thus
    * created. Returns True on success.
    *
    * @param userId
    *   User identifier of created sticker set owner
    * @param name
    *   Short name of sticker set, to be used in t.me/addstickers/ URLs (e.g., animals). Can contain only English
    *   letters, digits and underscores. Must begin with a letter, can't contain consecutive underscores and must end in
    *   "_by_<bot_username>". <bot_username> is case insensitive. 1-64 characters.
    * @param title
    *   Sticker set title, 1-64 characters
    * @param stickers
    *   A JSON-serialized list of 1-50 initial stickers to be added to the sticker set
    * @param stickerType
    *   Type of stickers in the set, pass “regular”, “mask”, or “custom_emoji”. By default, a regular sticker set is
    *   created.
    * @param needsRepainting
    *   Pass True if stickers in the sticker set must be repainted to the color of text when used in messages, the
    *   accent color if used as emoji status, white on chat photos, or another appropriate color based on context; for
    *   custom emoji sticker sets only
    */
  def createNewStickerSet(
    userId: Long,
    name: String,
    title: String,
    stickers: List[InputSticker] = List.empty,
    stickerType: Option[String] = Option.empty,
    needsRepainting: Option[Boolean] = Option.empty
  ): Method[Boolean] = {
    val req = CreateNewStickerSetReq(userId, name, title, stickers, stickerType, needsRepainting)
    MethodReq[Boolean]("createNewStickerSet", req.asJson)
  }

  /** Use this method to decline a chat join request. The bot must be an administrator in the chat for this to work and
    * must have the can_invite_users administrator right. Returns True on success.
    *
    * @param chatId
    *   Unique identifier for the target chat or username of the target channel (in the format &#064;channelusername)
    * @param userId
    *   Unique identifier of the target user
    */
  def declineChatJoinRequest(chatId: ChatId, userId: Long): Method[Boolean] = {
    val req = DeclineChatJoinRequestReq(chatId, userId)
    MethodReq[Boolean]("declineChatJoinRequest", req.asJson)
  }

  /** Delete messages on behalf of a business account. Requires the can_delete_sent_messages business bot right to
    * delete messages sent by the bot itself, or the can_delete_all_messages business bot right to delete any message.
    * Returns True on success.
    *
    * @param businessConnectionId
    *   Unique identifier of the business connection on behalf of which to delete the messages
    * @param messageIds
    *   A JSON-serialized list of 1-100 identifiers of messages to delete. All messages must be from the same chat. See
    *   deleteMessage for limitations on which messages can be deleted
    */
  def deleteBusinessMessages(businessConnectionId: String, messageIds: List[Int] = List.empty): Method[Boolean] = {
    val req = DeleteBusinessMessagesReq(businessConnectionId, messageIds)
    MethodReq[Boolean]("deleteBusinessMessages", req.asJson)
  }

  /** Use this method to delete a chat photo. Photos can't be changed for private chats. The bot must be an
    * administrator in the chat for this to work and must have the appropriate administrator rights. Returns True on
    * success.
    *
    * @param chatId
    *   Unique identifier for the target chat or username of the target channel (in the format &#064;channelusername)
    */
  def deleteChatPhoto(chatId: ChatId): Method[Boolean] = {
    val req = DeleteChatPhotoReq(chatId)
    MethodReq[Boolean]("deleteChatPhoto", req.asJson)
  }

  /** Use this method to delete a group sticker set from a supergroup. The bot must be an administrator in the chat for
    * this to work and must have the appropriate administrator rights. Use the field can_set_sticker_set optionally
    * returned in getChat requests to check if the bot can use this method. Returns True on success.
    *
    * @param chatId
    *   Unique identifier for the target chat or username of the target supergroup (in the format
    *   &#064;supergroupusername)
    */
  def deleteChatStickerSet(chatId: ChatId): Method[Boolean] = {
    val req = DeleteChatStickerSetReq(chatId)
    MethodReq[Boolean]("deleteChatStickerSet", req.asJson)
  }

  /** Use this method to delete a forum topic along with all its messages in a forum supergroup chat. The bot must be an
    * administrator in the chat for this to work and must have the can_delete_messages administrator rights. Returns
    * True on success.
    *
    * @param chatId
    *   Unique identifier for the target chat or username of the target supergroup (in the format
    *   &#064;supergroupusername)
    * @param messageThreadId
    *   Unique identifier for the target message thread of the forum topic
    */
  def deleteForumTopic(chatId: ChatId, messageThreadId: Int): Method[Boolean] = {
    val req = DeleteForumTopicReq(chatId, messageThreadId)
    MethodReq[Boolean]("deleteForumTopic", req.asJson)
  }

  /** Use this method to delete a message, including service messages, with the following limitations: - A message can
    * only be deleted if it was sent less than 48 hours ago. - Service messages about a supergroup, channel, or forum
    * topic creation can't be deleted. - A dice message in a private chat can only be deleted if it was sent more than
    * 24 hours ago. - Bots can delete outgoing messages in private chats, groups, and supergroups. - Bots can delete
    * incoming messages in private chats. - Bots granted can_post_messages permissions can delete outgoing messages in
    * channels. - If the bot is an administrator of a group, it can delete any message there. - If the bot has
    * can_delete_messages permission in a supergroup or a channel, it can delete any message there. Returns True on
    * success.
    *
    * @param chatId
    *   Unique identifier for the target chat or username of the target channel (in the format &#064;channelusername)
    * @param messageId
    *   Identifier of the message to delete
    */
  def deleteMessage(chatId: ChatId, messageId: Int): Method[Boolean] = {
    val req = DeleteMessageReq(chatId, messageId)
    MethodReq[Boolean]("deleteMessage", req.asJson)
  }

  /** Use this method to delete multiple messages simultaneously. If some of the specified messages can't be found, they
    * are skipped. Returns True on success.
    *
    * @param chatId
    *   Unique identifier for the target chat or username of the target channel (in the format &#064;channelusername)
    * @param messageIds
    *   A JSON-serialized list of 1-100 identifiers of messages to delete. See deleteMessage for limitations on which
    *   messages can be deleted
    */
  def deleteMessages(chatId: ChatId, messageIds: List[Int] = List.empty): Method[Boolean] = {
    val req = DeleteMessagesReq(chatId, messageIds)
    MethodReq[Boolean]("deleteMessages", req.asJson)
  }

  /** Use this method to delete the list of the bot's commands for the given scope and user language. After deletion,
    * higher level commands will be shown to affected users. Returns True on success.
    *
    * @param scope
    *   A JSON-serialized object, describing scope of users for which the commands are relevant. Defaults to
    *   BotCommandScopeDefault.
    * @param languageCode
    *   A two-letter ISO 639-1 language code. If empty, commands will be applied to all users from the given scope, for
    *   whose language there are no dedicated commands
    */
  def deleteMyCommands(
    scope: Option[BotCommandScope] = Option.empty,
    languageCode: Option[String] = Option.empty
  ): Method[Boolean] = {
    val req = DeleteMyCommandsReq(scope, languageCode)
    MethodReq[Boolean]("deleteMyCommands", req.asJson)
  }

  /** Use this method to delete a sticker from a set created by the bot. Returns True on success.
    *
    * @param sticker
    *   File identifier of the sticker
    */
  def deleteStickerFromSet(sticker: String): Method[Boolean] = {
    val req = DeleteStickerFromSetReq(sticker)
    MethodReq[Boolean]("deleteStickerFromSet", req.asJson)
  }

  /** Use this method to delete a sticker set that was created by the bot. Returns True on success.
    *
    * @param name
    *   Sticker set name
    */
  def deleteStickerSet(name: String): Method[Boolean] = {
    val req = DeleteStickerSetReq(name)
    MethodReq[Boolean]("deleteStickerSet", req.asJson)
  }

  /** Deletes a story previously posted by the bot on behalf of a managed business account. Requires the
    * can_manage_stories business bot right. Returns True on success.
    *
    * @param businessConnectionId
    *   Unique identifier of the business connection
    * @param storyId
    *   Unique identifier of the story to delete
    */
  def deleteStory(businessConnectionId: String, storyId: Int): Method[Boolean] = {
    val req = DeleteStoryReq(businessConnectionId, storyId)
    MethodReq[Boolean]("deleteStory", req.asJson)
  }

  /** Use this method to remove webhook integration if you decide to switch back to getUpdates. Returns True on success.
    *
    * @param dropPendingUpdates
    *   Pass True to drop all pending updates
    */
  def deleteWebhook(dropPendingUpdates: Option[Boolean] = Option.empty): Method[Boolean] = {
    val req = DeleteWebhookReq(dropPendingUpdates)
    MethodReq[Boolean]("deleteWebhook", req.asJson)
  }

  /** Use this method to edit a non-primary invite link created by the bot. The bot must be an administrator in the chat
    * for this to work and must have the appropriate administrator rights. Returns the edited invite link as a
    * ChatInviteLink object.
    *
    * @param chatId
    *   Unique identifier for the target chat or username of the target channel (in the format &#064;channelusername)
    * @param inviteLink
    *   The invite link to edit
    * @param name
    *   Invite link name; 0-32 characters
    * @param expireDate
    *   Point in time (Unix timestamp) when the link will expire
    * @param memberLimit
    *   The maximum number of users that can be members of the chat simultaneously after joining the chat via this
    *   invite link; 1-99999
    * @param createsJoinRequest
    *   True, if users joining the chat via the link need to be approved by chat administrators. If True, member_limit
    *   can't be specified
    */
  def editChatInviteLink(
    chatId: ChatId,
    inviteLink: String,
    name: Option[String] = Option.empty,
    expireDate: Option[Int] = Option.empty,
    memberLimit: Option[Int] = Option.empty,
    createsJoinRequest: Option[Boolean] = Option.empty
  ): Method[ChatInviteLink] = {
    val req = EditChatInviteLinkReq(chatId, inviteLink, name, expireDate, memberLimit, createsJoinRequest)
    MethodReq[ChatInviteLink]("editChatInviteLink", req.asJson)
  }

  /** Use this method to edit a subscription invite link created by the bot. The bot must have the can_invite_users
    * administrator rights. Returns the edited invite link as a ChatInviteLink object.
    *
    * @param chatId
    *   Unique identifier for the target chat or username of the target channel (in the format &#064;channelusername)
    * @param inviteLink
    *   The invite link to edit
    * @param name
    *   Invite link name; 0-32 characters
    */
  def editChatSubscriptionInviteLink(
    chatId: ChatId,
    inviteLink: String,
    name: Option[String] = Option.empty
  ): Method[ChatInviteLink] = {
    val req = EditChatSubscriptionInviteLinkReq(chatId, inviteLink, name)
    MethodReq[ChatInviteLink]("editChatSubscriptionInviteLink", req.asJson)
  }

  /** Use this method to edit name and icon of a topic in a forum supergroup chat. The bot must be an administrator in
    * the chat for this to work and must have the can_manage_topics administrator rights, unless it is the creator of
    * the topic. Returns True on success.
    *
    * @param chatId
    *   Unique identifier for the target chat or username of the target supergroup (in the format
    *   &#064;supergroupusername)
    * @param messageThreadId
    *   Unique identifier for the target message thread of the forum topic
    * @param name
    *   New topic name, 0-128 characters. If not specified or empty, the current name of the topic will be kept
    * @param iconCustomEmojiId
    *   New unique identifier of the custom emoji shown as the topic icon. Use getForumTopicIconStickers to get all
    *   allowed custom emoji identifiers. Pass an empty string to remove the icon. If not specified, the current icon
    *   will be kept
    */
  def editForumTopic(
    chatId: ChatId,
    messageThreadId: Int,
    name: Option[String] = Option.empty,
    iconCustomEmojiId: Option[String] = Option.empty
  ): Method[Boolean] = {
    val req = EditForumTopicReq(chatId, messageThreadId, name, iconCustomEmojiId)
    MethodReq[Boolean]("editForumTopic", req.asJson)
  }

  /** Use this method to edit the name of the 'General' topic in a forum supergroup chat. The bot must be an
    * administrator in the chat for this to work and must have the can_manage_topics administrator rights. Returns True
    * on success.
    *
    * @param chatId
    *   Unique identifier for the target chat or username of the target supergroup (in the format
    *   &#064;supergroupusername)
    * @param name
    *   New topic name, 1-128 characters
    */
  def editGeneralForumTopic(chatId: ChatId, name: String): Method[Boolean] = {
    val req = EditGeneralForumTopicReq(chatId, name)
    MethodReq[Boolean]("editGeneralForumTopic", req.asJson)
  }

  /** Use this method to edit captions of messages. On success, if the edited message is not an inline message, the
    * edited Message is returned, otherwise True is returned. Note that business messages that were not sent by the bot
    * and do not contain an inline keyboard can only be edited within 48 hours from the time they were sent.
    *
    * @param businessConnectionId
    *   Unique identifier of the business connection on behalf of which the message to be edited was sent
    * @param chatId
    *   Required if inline_message_id is not specified. Unique identifier for the target chat or username of the target
    *   channel (in the format &#064;channelusername)
    * @param messageId
    *   Required if inline_message_id is not specified. Identifier of the message to edit
    * @param inlineMessageId
    *   Required if chat_id and message_id are not specified. Identifier of the inline message
    * @param caption
    *   New caption of the message, 0-1024 characters after entities parsing
    * @param parseMode
    *   Mode for parsing entities in the message caption. See formatting options for more details.
    * @param captionEntities
    *   A JSON-serialized list of special entities that appear in the caption, which can be specified instead of
    *   parse_mode
    * @param showCaptionAboveMedia
    *   Pass True, if the caption must be shown above the message media. Supported only for animation, photo and video
    *   messages.
    * @param replyMarkup
    *   A JSON-serialized object for an inline keyboard.
    */
  def editMessageCaption(
    businessConnectionId: Option[String] = Option.empty,
    chatId: Option[ChatId] = Option.empty,
    messageId: Option[Int] = Option.empty,
    inlineMessageId: Option[String] = Option.empty,
    caption: Option[String] = Option.empty,
    parseMode: Option[ParseMode] = Option.empty,
    captionEntities: List[MessageEntity] = List.empty,
    showCaptionAboveMedia: Option[Boolean] = Option.empty,
    replyMarkup: Option[InlineKeyboardMarkup] = Option.empty
  ): Method[Either[Boolean, Message]] = {
    val req = EditMessageCaptionReq(
      businessConnectionId,
      chatId,
      messageId,
      inlineMessageId,
      caption,
      parseMode,
      captionEntities,
      showCaptionAboveMedia,
      replyMarkup
    )
    MethodReq[Either[Boolean, Message]]("editMessageCaption", req.asJson)
  }

  /** Use this method to edit a checklist on behalf of a connected business account. On success, the edited Message is
    * returned.
    *
    * @param businessConnectionId
    *   Unique identifier of the business connection on behalf of which the message will be sent
    * @param chatId
    *   Unique identifier for the target chat
    * @param messageId
    *   Unique identifier for the target message
    * @param checklist
    *   A JSON-serialized object for the new checklist
    * @param replyMarkup
    *   A JSON-serialized object for the new inline keyboard for the message
    */
  def editMessageChecklist(
    businessConnectionId: String,
    chatId: Long,
    messageId: Int,
    checklist: InputChecklist,
    replyMarkup: Option[InlineKeyboardMarkup] = Option.empty
  ): Method[Message] = {
    val req = EditMessageChecklistReq(businessConnectionId, chatId, messageId, checklist, replyMarkup)
    MethodReq[Message]("editMessageChecklist", req.asJson)
  }

  /** Use this method to edit live location messages. A location can be edited until its live_period expires or editing
    * is explicitly disabled by a call to stopMessageLiveLocation. On success, if the edited message is not an inline
    * message, the edited Message is returned, otherwise True is returned.
    *
    * @param latitude
    *   Latitude of new location
    * @param longitude
    *   Longitude of new location
    * @param businessConnectionId
    *   Unique identifier of the business connection on behalf of which the message to be edited was sent
    * @param chatId
    *   Required if inline_message_id is not specified. Unique identifier for the target chat or username of the target
    *   channel (in the format &#064;channelusername)
    * @param messageId
    *   Required if inline_message_id is not specified. Identifier of the message to edit
    * @param inlineMessageId
    *   Required if chat_id and message_id are not specified. Identifier of the inline message
    * @param livePeriod
    *   New period in seconds during which the location can be updated, starting from the message send date. If
    *   0x7FFFFFFF is specified, then the location can be updated forever. Otherwise, the new value must not exceed the
    *   current live_period by more than a day, and the live location expiration date must remain within the next 90
    *   days. If not specified, then live_period remains unchanged
    * @param horizontalAccuracy
    *   The radius of uncertainty for the location, measured in meters; 0-1500
    * @param heading
    *   Direction in which the user is moving, in degrees. Must be between 1 and 360 if specified.
    * @param proximityAlertRadius
    *   The maximum distance for proximity alerts about approaching another chat member, in meters. Must be between 1
    *   and 100000 if specified.
    * @param replyMarkup
    *   A JSON-serialized object for a new inline keyboard.
    */
  def editMessageLiveLocation(
    latitude: Float,
    longitude: Float,
    businessConnectionId: Option[String] = Option.empty,
    chatId: Option[ChatId] = Option.empty,
    messageId: Option[Int] = Option.empty,
    inlineMessageId: Option[String] = Option.empty,
    livePeriod: Option[Int] = Option.empty,
    horizontalAccuracy: Option[Float] = Option.empty,
    heading: Option[Int] = Option.empty,
    proximityAlertRadius: Option[Int] = Option.empty,
    replyMarkup: Option[InlineKeyboardMarkup] = Option.empty
  ): Method[Either[Boolean, Message]] = {
    val req = EditMessageLiveLocationReq(
      latitude,
      longitude,
      businessConnectionId,
      chatId,
      messageId,
      inlineMessageId,
      livePeriod,
      horizontalAccuracy,
      heading,
      proximityAlertRadius,
      replyMarkup
    )
    MethodReq[Either[Boolean, Message]]("editMessageLiveLocation", req.asJson)
  }

  /** Use this method to edit animation, audio, document, photo, or video messages, or to add media to text messages. If
    * a message is part of a message album, then it can be edited only to an audio for audio albums, only to a document
    * for document albums and to a photo or a video otherwise. When an inline message is edited, a new file can't be
    * uploaded; use a previously uploaded file via its file_id or specify a URL. On success, if the edited message is
    * not an inline message, the edited Message is returned, otherwise True is returned. Note that business messages
    * that were not sent by the bot and do not contain an inline keyboard can only be edited within 48 hours from the
    * time they were sent.
    *
    * @param media
    *   A JSON-serialized object for a new media content of the message
    * @param businessConnectionId
    *   Unique identifier of the business connection on behalf of which the message to be edited was sent
    * @param chatId
    *   Required if inline_message_id is not specified. Unique identifier for the target chat or username of the target
    *   channel (in the format &#064;channelusername)
    * @param messageId
    *   Required if inline_message_id is not specified. Identifier of the message to edit
    * @param inlineMessageId
    *   Required if chat_id and message_id are not specified. Identifier of the inline message
    * @param replyMarkup
    *   A JSON-serialized object for a new inline keyboard.
    */
  def editMessageMedia(
    media: InputMedia,
    businessConnectionId: Option[String] = Option.empty,
    chatId: Option[ChatId] = Option.empty,
    messageId: Option[Int] = Option.empty,
    inlineMessageId: Option[String] = Option.empty,
    replyMarkup: Option[InlineKeyboardMarkup] = Option.empty
  ): Method[Either[Boolean, Message]] = {
    val req = EditMessageMediaReq(media, businessConnectionId, chatId, messageId, inlineMessageId, replyMarkup)
    MethodReq[Either[Boolean, Message]]("editMessageMedia", req.asJson)
  }

  /** Use this method to edit only the reply markup of messages. On success, if the edited message is not an inline
    * message, the edited Message is returned, otherwise True is returned. Note that business messages that were not
    * sent by the bot and do not contain an inline keyboard can only be edited within 48 hours from the time they were
    * sent.
    *
    * @param businessConnectionId
    *   Unique identifier of the business connection on behalf of which the message to be edited was sent
    * @param chatId
    *   Required if inline_message_id is not specified. Unique identifier for the target chat or username of the target
    *   channel (in the format &#064;channelusername)
    * @param messageId
    *   Required if inline_message_id is not specified. Identifier of the message to edit
    * @param inlineMessageId
    *   Required if chat_id and message_id are not specified. Identifier of the inline message
    * @param replyMarkup
    *   A JSON-serialized object for an inline keyboard.
    */
  def editMessageReplyMarkup(
    businessConnectionId: Option[String] = Option.empty,
    chatId: Option[ChatId] = Option.empty,
    messageId: Option[Int] = Option.empty,
    inlineMessageId: Option[String] = Option.empty,
    replyMarkup: Option[InlineKeyboardMarkup] = Option.empty
  ): Method[Either[Boolean, Message]] = {
    val req = EditMessageReplyMarkupReq(businessConnectionId, chatId, messageId, inlineMessageId, replyMarkup)
    MethodReq[Either[Boolean, Message]]("editMessageReplyMarkup", req.asJson)
  }

  /** Use this method to edit text and game messages. On success, if the edited message is not an inline message, the
    * edited Message is returned, otherwise True is returned. Note that business messages that were not sent by the bot
    * and do not contain an inline keyboard can only be edited within 48 hours from the time they were sent.
    *
    * @param text
    *   New text of the message, 1-4096 characters after entities parsing
    * @param businessConnectionId
    *   Unique identifier of the business connection on behalf of which the message to be edited was sent
    * @param chatId
    *   Required if inline_message_id is not specified. Unique identifier for the target chat or username of the target
    *   channel (in the format &#064;channelusername)
    * @param messageId
    *   Required if inline_message_id is not specified. Identifier of the message to edit
    * @param inlineMessageId
    *   Required if chat_id and message_id are not specified. Identifier of the inline message
    * @param parseMode
    *   Mode for parsing entities in the message text. See formatting options for more details.
    * @param entities
    *   A JSON-serialized list of special entities that appear in message text, which can be specified instead of
    *   parse_mode
    * @param linkPreviewOptions
    *   Link preview generation options for the message
    * @param replyMarkup
    *   A JSON-serialized object for an inline keyboard.
    */
  def editMessageText(
    text: String,
    businessConnectionId: Option[String] = Option.empty,
    chatId: Option[ChatId] = Option.empty,
    messageId: Option[Int] = Option.empty,
    inlineMessageId: Option[String] = Option.empty,
    parseMode: Option[ParseMode] = Option.empty,
    entities: List[MessageEntity] = List.empty,
    linkPreviewOptions: Option[LinkPreviewOptions] = Option.empty,
    replyMarkup: Option[InlineKeyboardMarkup] = Option.empty
  ): Method[Either[Boolean, Message]] = {
    val req = EditMessageTextReq(
      text,
      businessConnectionId,
      chatId,
      messageId,
      inlineMessageId,
      parseMode,
      entities,
      linkPreviewOptions,
      replyMarkup
    )
    MethodReq[Either[Boolean, Message]]("editMessageText", req.asJson)
  }

  /** Edits a story previously posted by the bot on behalf of a managed business account. Requires the
    * can_manage_stories business bot right. Returns Story on success.
    *
    * @param businessConnectionId
    *   Unique identifier of the business connection
    * @param storyId
    *   Unique identifier of the story to edit
    * @param content
    *   Content of the story
    * @param caption
    *   Caption of the story, 0-2048 characters after entities parsing
    * @param parseMode
    *   Mode for parsing entities in the story caption. See formatting options for more details.
    * @param captionEntities
    *   A JSON-serialized list of special entities that appear in the caption, which can be specified instead of
    *   parse_mode
    * @param areas
    *   A JSON-serialized list of clickable areas to be shown on the story
    */
  def editStory(
    businessConnectionId: String,
    storyId: Int,
    content: InputStoryContent,
    caption: Option[String] = Option.empty,
    parseMode: Option[ParseMode] = Option.empty,
    captionEntities: List[MessageEntity] = List.empty,
    areas: List[StoryArea] = List.empty
  ): Method[Story] = {
    val req = EditStoryReq(businessConnectionId, storyId, content, caption, parseMode, captionEntities, areas)
    MethodReq[Story]("editStory", req.asJson)
  }

  /** Allows the bot to cancel or re-enable extension of a subscription paid in Telegram Stars. Returns True on success.
    *
    * @param userId
    *   Identifier of the user whose subscription will be edited
    * @param telegramPaymentChargeId
    *   Telegram payment identifier for the subscription
    * @param isCanceled
    *   Pass True to cancel extension of the user subscription; the subscription must be active up to the end of the
    *   current subscription period. Pass False to allow the user to re-enable a subscription that was previously
    *   canceled by the bot.
    */
  def editUserStarSubscription(userId: Long, telegramPaymentChargeId: String, isCanceled: Boolean): Method[Boolean] = {
    val req = EditUserStarSubscriptionReq(userId, telegramPaymentChargeId, isCanceled)
    MethodReq[Boolean]("editUserStarSubscription", req.asJson)
  }

  /** Use this method to generate a new primary invite link for a chat; any previously generated primary link is
    * revoked. The bot must be an administrator in the chat for this to work and must have the appropriate administrator
    * rights. Returns the new invite link as String on success.
    *
    * @param chatId
    *   Unique identifier for the target chat or username of the target channel (in the format &#064;channelusername)
    */
  def exportChatInviteLink(chatId: ChatId): Method[String] = {
    val req = ExportChatInviteLinkReq(chatId)
    MethodReq[String]("exportChatInviteLink", req.asJson)
  }

  /** Use this method to forward messages of any kind. Service messages and messages with protected content can't be
    * forwarded. On success, the sent Message is returned.
    *
    * @param chatId
    *   Unique identifier for the target chat or username of the target channel (in the format &#064;channelusername)
    * @param fromChatId
    *   Unique identifier for the chat where the original message was sent (or channel username in the format
    *   &#064;channelusername)
    * @param messageId
    *   Message identifier in the chat specified in from_chat_id
    * @param messageThreadId
    *   Unique identifier for the target message thread (topic) of the forum; for forum supergroups only
    * @param videoStartTimestamp
    *   New start timestamp for the forwarded video in the message
    * @param disableNotification
    *   Sends the message silently. Users will receive a notification with no sound.
    * @param protectContent
    *   Protects the contents of the forwarded message from forwarding and saving
    */
  def forwardMessage(
    chatId: ChatId,
    fromChatId: ChatId,
    messageId: Int,
    messageThreadId: Option[Int] = Option.empty,
    videoStartTimestamp: Option[Int] = Option.empty,
    disableNotification: Option[Boolean] = Option.empty,
    protectContent: Option[Boolean] = Option.empty
  ): Method[Message] = {
    val req = ForwardMessageReq(
      chatId,
      fromChatId,
      messageId,
      messageThreadId,
      videoStartTimestamp,
      disableNotification,
      protectContent
    )
    MethodReq[Message]("forwardMessage", req.asJson)
  }

  /** Use this method to forward multiple messages of any kind. If some of the specified messages can't be found or
    * forwarded, they are skipped. Service messages and messages with protected content can't be forwarded. Album
    * grouping is kept for forwarded messages. On success, an array of MessageId of the sent messages is returned.
    *
    * @param chatId
    *   Unique identifier for the target chat or username of the target channel (in the format &#064;channelusername)
    * @param fromChatId
    *   Unique identifier for the chat where the original messages were sent (or channel username in the format
    *   &#064;channelusername)
    * @param messageThreadId
    *   Unique identifier for the target message thread (topic) of the forum; for forum supergroups only
    * @param messageIds
    *   A JSON-serialized list of 1-100 identifiers of messages in the chat from_chat_id to forward. The identifiers
    *   must be specified in a strictly increasing order.
    * @param disableNotification
    *   Sends the messages silently. Users will receive a notification with no sound.
    * @param protectContent
    *   Protects the contents of the forwarded messages from forwarding and saving
    */
  def forwardMessages(
    chatId: ChatId,
    fromChatId: ChatId,
    messageThreadId: Option[Int] = Option.empty,
    messageIds: List[Int] = List.empty,
    disableNotification: Option[Boolean] = Option.empty,
    protectContent: Option[Boolean] = Option.empty
  ): Method[List[MessageId]] = {
    val req = ForwardMessagesReq(chatId, fromChatId, messageThreadId, messageIds, disableNotification, protectContent)
    MethodReq[List[MessageId]]("forwardMessages", req.asJson)
  }

  /** Returns the list of gifts that can be sent by the bot to users and channel chats. Requires no parameters. Returns
    * a Gifts object.
    */
  def getAvailableGifts(): Method[Gifts] = {
    val req = GetAvailableGiftsReq
    MethodReq[Gifts]("getAvailableGifts", req.asJson)
  }

  /** Returns the gifts received and owned by a managed business account. Requires the can_view_gifts_and_stars business
    * bot right. Returns OwnedGifts on success.
    *
    * @param businessConnectionId
    *   Unique identifier of the business connection
    * @param excludeUnsaved
    *   Pass True to exclude gifts that aren't saved to the account's profile page
    * @param excludeSaved
    *   Pass True to exclude gifts that are saved to the account's profile page
    * @param excludeUnlimited
    *   Pass True to exclude gifts that can be purchased an unlimited number of times
    * @param excludeLimited
    *   Pass True to exclude gifts that can be purchased a limited number of times
    * @param excludeUnique
    *   Pass True to exclude unique gifts
    * @param sortByPrice
    *   Pass True to sort results by gift price instead of send date. Sorting is applied before pagination.
    * @param offset
    *   Offset of the first entry to return as received from the previous request; use empty string to get the first
    *   chunk of results
    * @param limit
    *   The maximum number of gifts to be returned; 1-100. Defaults to 100
    */
  def getBusinessAccountGifts(
    businessConnectionId: String,
    excludeUnsaved: Option[Boolean] = Option.empty,
    excludeSaved: Option[Boolean] = Option.empty,
    excludeUnlimited: Option[Boolean] = Option.empty,
    excludeLimited: Option[Boolean] = Option.empty,
    excludeUnique: Option[Boolean] = Option.empty,
    sortByPrice: Option[Boolean] = Option.empty,
    offset: Option[String] = Option.empty,
    limit: Option[Int] = Option.empty
  ): Method[OwnedGifts] = {
    val req = GetBusinessAccountGiftsReq(
      businessConnectionId,
      excludeUnsaved,
      excludeSaved,
      excludeUnlimited,
      excludeLimited,
      excludeUnique,
      sortByPrice,
      offset,
      limit
    )
    MethodReq[OwnedGifts]("getBusinessAccountGifts", req.asJson)
  }

  /** Returns the amount of Telegram Stars owned by a managed business account. Requires the can_view_gifts_and_stars
    * business bot right. Returns StarAmount on success.
    *
    * @param businessConnectionId
    *   Unique identifier of the business connection
    */
  def getBusinessAccountStarBalance(businessConnectionId: String): Method[StarAmount] = {
    val req = GetBusinessAccountStarBalanceReq(businessConnectionId)
    MethodReq[StarAmount]("getBusinessAccountStarBalance", req.asJson)
  }

  /** Use this method to get information about the connection of the bot with a business account. Returns a
    * BusinessConnection object on success.
    *
    * @param businessConnectionId
    *   Unique identifier of the business connection
    */
  def getBusinessConnection(businessConnectionId: String): Method[BusinessConnection] = {
    val req = GetBusinessConnectionReq(businessConnectionId)
    MethodReq[BusinessConnection]("getBusinessConnection", req.asJson)
  }

  /** Use this method to get up-to-date information about the chat. Returns a ChatFullInfo object on success.
    *
    * @param chatId
    *   Unique identifier for the target chat or username of the target supergroup or channel (in the format
    *   &#064;channelusername)
    */
  def getChat(chatId: ChatId): Method[ChatFullInfo] = {
    val req = GetChatReq(chatId)
    MethodReq[ChatFullInfo]("getChat", req.asJson)
  }

  /** Use this method to get a list of administrators in a chat, which aren't bots. Returns an Array of ChatMember
    * objects.
    *
    * @param chatId
    *   Unique identifier for the target chat or username of the target supergroup or channel (in the format
    *   &#064;channelusername)
    */
  def getChatAdministrators(chatId: ChatId): Method[List[iozhik.OpenEnum[ChatMember]]] = {
    val req = GetChatAdministratorsReq(chatId)
    MethodReq[List[iozhik.OpenEnum[ChatMember]]]("getChatAdministrators", req.asJson)
  }

  /** Use this method to get information about a member of a chat. The method is only guaranteed to work for other users
    * if the bot is an administrator in the chat. Returns a ChatMember object on success.
    *
    * @param chatId
    *   Unique identifier for the target chat or username of the target supergroup or channel (in the format
    *   &#064;channelusername)
    * @param userId
    *   Unique identifier of the target user
    */
  def getChatMember(chatId: ChatId, userId: Long): Method[iozhik.OpenEnum[ChatMember]] = {
    val req = GetChatMemberReq(chatId, userId)
    MethodReq[iozhik.OpenEnum[ChatMember]]("getChatMember", req.asJson)
  }

  /** Use this method to get the number of members in a chat. Returns Int on success.
    *
    * @param chatId
    *   Unique identifier for the target chat or username of the target supergroup or channel (in the format
    *   &#064;channelusername)
    */
  def getChatMemberCount(chatId: ChatId): Method[Int] = {
    val req = GetChatMemberCountReq(chatId)
    MethodReq[Int]("getChatMemberCount", req.asJson)
  }

  /** Use this method to get the current value of the bot's menu button in a private chat, or the default menu button.
    * Returns MenuButton on success.
    *
    * @param chatId
    *   Unique identifier for the target private chat. If not specified, default bot's menu button will be returned
    */
  def getChatMenuButton(chatId: Option[Long] = Option.empty): Method[iozhik.OpenEnum[MenuButton]] = {
    val req = GetChatMenuButtonReq(chatId)
    MethodReq[iozhik.OpenEnum[MenuButton]]("getChatMenuButton", req.asJson)
  }

  /** Use this method to get information about custom emoji stickers by their identifiers. Returns an Array of Sticker
    * objects.
    *
    * @param customEmojiIds
    *   A JSON-serialized list of custom emoji identifiers. At most 200 custom emoji identifiers can be specified.
    */
  def getCustomEmojiStickers(customEmojiIds: List[String] = List.empty): Method[List[Sticker]] = {
    val req = GetCustomEmojiStickersReq(customEmojiIds)
    MethodReq[List[Sticker]]("getCustomEmojiStickers", req.asJson)
  }

  /** Use this method to get basic information about a file and prepare it for downloading. For the moment, bots can
    * download files of up to 20MB in size. On success, a File object is returned. The file can then be downloaded via
    * the link https://api.telegram.org/file/bot<token>/<file_path>, where <file_path> is taken from the response. It is
    * guaranteed that the link will be valid for at least 1 hour. When the link expires, a new one can be requested by
    * calling getFile again.
    *
    * @param fileId
    *   File identifier to get information about
    */
  def getFile(fileId: String): Method[File] = {
    val req = GetFileReq(fileId)
    MethodReq[File]("getFile", req.asJson)
  }

  /** Use this method to get custom emoji stickers, which can be used as a forum topic icon by any user. Requires no
    * parameters. Returns an Array of Sticker objects.
    */
  def getForumTopicIconStickers(): Method[List[Sticker]] = {
    val req = GetForumTopicIconStickersReq
    MethodReq[List[Sticker]]("getForumTopicIconStickers", req.asJson)
  }

  /** Use this method to get data for high score tables. Will return the score of the specified user and several of
    * their neighbors in a game. Returns an Array of GameHighScore objects.
    *
    * @param userId
    *   Target user id
    * @param chatId
    *   Required if inline_message_id is not specified. Unique identifier for the target chat
    * @param messageId
    *   Required if inline_message_id is not specified. Identifier of the sent message
    * @param inlineMessageId
    *   Required if chat_id and message_id are not specified. Identifier of the inline message
    */
  def getGameHighScores(
    userId: Long,
    chatId: Option[Long] = Option.empty,
    messageId: Option[Int] = Option.empty,
    inlineMessageId: Option[String] = Option.empty
  ): Method[List[GameHighScore]] = {
    val req = GetGameHighScoresReq(userId, chatId, messageId, inlineMessageId)
    MethodReq[List[GameHighScore]]("getGameHighScores", req.asJson)
  }

  /** A simple method for testing your bot's authentication token. Requires no parameters. Returns basic information
    * about the bot in form of a User object.
    */
  def getMe(): Method[User] = {
    val req = GetMeReq
    MethodReq[User]("getMe", req.asJson)
  }

  /** Use this method to get the current list of the bot's commands for the given scope and user language. Returns an
    * Array of BotCommand objects. If commands aren't set, an empty list is returned.
    *
    * @param scope
    *   A JSON-serialized object, describing scope of users. Defaults to BotCommandScopeDefault.
    * @param languageCode
    *   A two-letter ISO 639-1 language code or an empty string
    */
  def getMyCommands(
    scope: Option[BotCommandScope] = Option.empty,
    languageCode: Option[String] = Option.empty
  ): Method[List[BotCommand]] = {
    val req = GetMyCommandsReq(scope, languageCode)
    MethodReq[List[BotCommand]]("getMyCommands", req.asJson)
  }

  /** Use this method to get the current default administrator rights of the bot. Returns ChatAdministratorRights on
    * success.
    *
    * @param forChannels
    *   Pass True to get default administrator rights of the bot in channels. Otherwise, default administrator rights of
    *   the bot for groups and supergroups will be returned.
    */
  def getMyDefaultAdministratorRights(forChannels: Option[Boolean] = Option.empty): Method[ChatAdministratorRights] = {
    val req = GetMyDefaultAdministratorRightsReq(forChannels)
    MethodReq[ChatAdministratorRights]("getMyDefaultAdministratorRights", req.asJson)
  }

  /** Use this method to get the current bot description for the given user language. Returns BotDescription on success.
    *
    * @param languageCode
    *   A two-letter ISO 639-1 language code or an empty string
    */
  def getMyDescription(languageCode: Option[String] = Option.empty): Method[BotDescription] = {
    val req = GetMyDescriptionReq(languageCode)
    MethodReq[BotDescription]("getMyDescription", req.asJson)
  }

  /** Use this method to get the current bot name for the given user language. Returns BotName on success.
    *
    * @param languageCode
    *   A two-letter ISO 639-1 language code or an empty string
    */
  def getMyName(languageCode: Option[String] = Option.empty): Method[BotName] = {
    val req = GetMyNameReq(languageCode)
    MethodReq[BotName]("getMyName", req.asJson)
  }

  /** Use this method to get the current bot short description for the given user language. Returns BotShortDescription
    * on success.
    *
    * @param languageCode
    *   A two-letter ISO 639-1 language code or an empty string
    */
  def getMyShortDescription(languageCode: Option[String] = Option.empty): Method[BotShortDescription] = {
    val req = GetMyShortDescriptionReq(languageCode)
    MethodReq[BotShortDescription]("getMyShortDescription", req.asJson)
  }

  /** A method to get the current Telegram Stars balance of the bot. Requires no parameters. On success, returns a
    * StarAmount object.
    */
  def getMyStarBalance(): Method[StarAmount] = {
    val req = GetMyStarBalanceReq
    MethodReq[StarAmount]("getMyStarBalance", req.asJson)
  }

  /** Returns the bot's Telegram Star transactions in chronological order. On success, returns a StarTransactions
    * object.
    *
    * @param offset
    *   Number of transactions to skip in the response
    * @param limit
    *   The maximum number of transactions to be retrieved. Values between 1-100 are accepted. Defaults to 100.
    */
  def getStarTransactions(
    offset: Option[Int] = Option.empty,
    limit: Option[Int] = Option.empty
  ): Method[StarTransactions] = {
    val req = GetStarTransactionsReq(offset, limit)
    MethodReq[StarTransactions]("getStarTransactions", req.asJson)
  }

  /** Use this method to get a sticker set. On success, a StickerSet object is returned.
    *
    * @param name
    *   Name of the sticker set
    */
  def getStickerSet(name: String): Method[StickerSet] = {
    val req = GetStickerSetReq(name)
    MethodReq[StickerSet]("getStickerSet", req.asJson)
  }

  /** Use this method to receive incoming updates using long polling (wiki). Returns an Array of Update objects.
    *
    * @param offset
    *   Identifier of the first update to be returned. Must be greater by one than the highest among the identifiers of
    *   previously received updates. By default, updates starting with the earliest unconfirmed update are returned. An
    *   update is considered confirmed as soon as getUpdates is called with an offset higher than its update_id. The
    *   negative offset can be specified to retrieve updates starting from -offset update from the end of the updates
    *   queue. All previous updates will be forgotten.
    * @param limit
    *   Limits the number of updates to be retrieved. Values between 1-100 are accepted. Defaults to 100.
    * @param timeout
    *   Timeout in seconds for long polling. Defaults to 0, i.e. usual short polling. Should be positive, short polling
    *   should be used for testing purposes only.
    * @param allowedUpdates
    *   A JSON-serialized list of the update types you want your bot to receive. For example, specify ["message",
    *   "edited_channel_post", "callback_query"] to only receive updates of these types. See Update for a complete list
    *   of available update types. Specify an empty list to receive all update types except chat_member,
    *   message_reaction, and message_reaction_count (default). If not specified, the previous setting will be used.
    *   Please note that this parameter doesn't affect updates created before the call to getUpdates, so unwanted
    *   updates may be received for a short period of time.
    */
  def getUpdates(
    offset: Option[Int] = Option.empty,
    limit: Option[Int] = Option.empty,
    timeout: Option[Int] = Option.empty,
    allowedUpdates: List[String] = List.empty
  ): Method[List[Update]] = {
    val req = GetUpdatesReq(offset, limit, timeout, allowedUpdates)
    MethodReq[List[Update]]("getUpdates", req.asJson)
  }

  /** Use this method to get the list of boosts added to a chat by a user. Requires administrator rights in the chat.
    * Returns a UserChatBoosts object.
    *
    * @param chatId
    *   Unique identifier for the chat or username of the channel (in the format &#064;channelusername)
    * @param userId
    *   Unique identifier of the target user
    */
  def getUserChatBoosts(chatId: ChatId, userId: Long): Method[UserChatBoosts] = {
    val req = GetUserChatBoostsReq(chatId, userId)
    MethodReq[UserChatBoosts]("getUserChatBoosts", req.asJson)
  }

  /** Use this method to get a list of profile pictures for a user. Returns a UserProfilePhotos object.
    *
    * @param userId
    *   Unique identifier of the target user
    * @param offset
    *   Sequential number of the first photo to be returned. By default, all photos are returned.
    * @param limit
    *   Limits the number of photos to be retrieved. Values between 1-100 are accepted. Defaults to 100.
    */
  def getUserProfilePhotos(
    userId: Long,
    offset: Option[Int] = Option.empty,
    limit: Option[Int] = Option.empty
  ): Method[UserProfilePhotos] = {
    val req = GetUserProfilePhotosReq(userId, offset, limit)
    MethodReq[UserProfilePhotos]("getUserProfilePhotos", req.asJson)
  }

  /** Use this method to get current webhook status. Requires no parameters. On success, returns a WebhookInfo object.
    * If the bot is using getUpdates, will return an object with the url field empty.
    */
  def getWebhookInfo(): Method[WebhookInfo] = {
    val req = GetWebhookInfoReq
    MethodReq[WebhookInfo]("getWebhookInfo", req.asJson)
  }

  /** Gifts a Telegram Premium subscription to the given user. Returns True on success.
    *
    * @param userId
    *   Unique identifier of the target user who will receive a Telegram Premium subscription
    * @param monthCount
    *   Number of months the Telegram Premium subscription will be active for the user; must be one of 3, 6, or 12
    * @param starCount
    *   Number of Telegram Stars to pay for the Telegram Premium subscription; must be 1000 for 3 months, 1500 for 6
    *   months, and 2500 for 12 months
    * @param text
    *   Text that will be shown along with the service message about the subscription; 0-128 characters
    * @param textParseMode
    *   Mode for parsing entities in the text. See formatting options for more details. Entities other than “bold”,
    *   “italic”, “underline”, “strikethrough”, “spoiler”, and “custom_emoji” are ignored.
    * @param textEntities
    *   A JSON-serialized list of special entities that appear in the gift text. It can be specified instead of
    *   text_parse_mode. Entities other than “bold”, “italic”, “underline”, “strikethrough”, “spoiler”, and
    *   “custom_emoji” are ignored.
    */
  def giftPremiumSubscription(
    userId: Long,
    monthCount: Int,
    starCount: Int,
    text: Option[String] = Option.empty,
    textParseMode: Option[ParseMode] = Option.empty,
    textEntities: List[MessageEntity] = List.empty
  ): Method[Boolean] = {
    val req = GiftPremiumSubscriptionReq(userId, monthCount, starCount, text, textParseMode, textEntities)
    MethodReq[Boolean]("giftPremiumSubscription", req.asJson)
  }

  /** Use this method to hide the 'General' topic in a forum supergroup chat. The bot must be an administrator in the
    * chat for this to work and must have the can_manage_topics administrator rights. The topic will be automatically
    * closed if it was open. Returns True on success.
    *
    * @param chatId
    *   Unique identifier for the target chat or username of the target supergroup (in the format
    *   &#064;supergroupusername)
    */
  def hideGeneralForumTopic(chatId: ChatId): Method[Boolean] = {
    val req = HideGeneralForumTopicReq(chatId)
    MethodReq[Boolean]("hideGeneralForumTopic", req.asJson)
  }

  /** Use this method for your bot to leave a group, supergroup or channel. Returns True on success.
    *
    * @param chatId
    *   Unique identifier for the target chat or username of the target supergroup or channel (in the format
    *   &#064;channelusername)
    */
  def leaveChat(chatId: ChatId): Method[Boolean] = {
    val req = LeaveChatReq(chatId)
    MethodReq[Boolean]("leaveChat", req.asJson)
  }

  /** Use this method to log out from the cloud Bot API server before launching the bot locally. You must log out the
    * bot before running it locally, otherwise there is no guarantee that the bot will receive updates. After a
    * successful call, you can immediately log in on a local server, but will not be able to log in back to the cloud
    * Bot API server for 10 minutes. Returns True on success. Requires no parameters.
    */
  def logOut(): Method[Boolean] = {
    val req = LogOutReq
    MethodReq[Boolean]("logOut", req.asJson)
  }

  /** Use this method to add a message to the list of pinned messages in a chat. If the chat is not a private chat, the
    * bot must be an administrator in the chat for this to work and must have the 'can_pin_messages' administrator right
    * in a supergroup or 'can_edit_messages' administrator right in a channel. Returns True on success.
    *
    * @param chatId
    *   Unique identifier for the target chat or username of the target channel (in the format &#064;channelusername)
    * @param messageId
    *   Identifier of a message to pin
    * @param businessConnectionId
    *   Unique identifier of the business connection on behalf of which the message will be pinned
    * @param disableNotification
    *   Pass True if it is not necessary to send a notification to all chat members about the new pinned message.
    *   Notifications are always disabled in channels and private chats.
    */
  def pinChatMessage(
    chatId: ChatId,
    messageId: Int,
    businessConnectionId: Option[String] = Option.empty,
    disableNotification: Option[Boolean] = Option.empty
  ): Method[Boolean] = {
    val req = PinChatMessageReq(chatId, messageId, businessConnectionId, disableNotification)
    MethodReq[Boolean]("pinChatMessage", req.asJson)
  }

  /** Posts a story on behalf of a managed business account. Requires the can_manage_stories business bot right. Returns
    * Story on success.
    *
    * @param businessConnectionId
    *   Unique identifier of the business connection
    * @param content
    *   Content of the story
    * @param activePeriod
    *   Period after which the story is moved to the archive, in seconds; must be one of 6 * 3600, 12 * 3600, 86400, or
    *   2 * 86400
    * @param caption
    *   Caption of the story, 0-2048 characters after entities parsing
    * @param parseMode
    *   Mode for parsing entities in the story caption. See formatting options for more details.
    * @param captionEntities
    *   A JSON-serialized list of special entities that appear in the caption, which can be specified instead of
    *   parse_mode
    * @param areas
    *   A JSON-serialized list of clickable areas to be shown on the story
    * @param postToChatPage
    *   Pass True to keep the story accessible after it expires
    * @param protectContent
    *   Pass True if the content of the story must be protected from forwarding and screenshotting
    */
  def postStory(
    businessConnectionId: String,
    content: InputStoryContent,
    activePeriod: Int,
    caption: Option[String] = Option.empty,
    parseMode: Option[ParseMode] = Option.empty,
    captionEntities: List[MessageEntity] = List.empty,
    areas: List[StoryArea] = List.empty,
    postToChatPage: Option[Boolean] = Option.empty,
    protectContent: Option[Boolean] = Option.empty
  ): Method[Story] = {
    val req = PostStoryReq(
      businessConnectionId,
      content,
      activePeriod,
      caption,
      parseMode,
      captionEntities,
      areas,
      postToChatPage,
      protectContent
    )
    MethodReq[Story]("postStory", req.asJson)
  }

  /** Use this method to promote or demote a user in a supergroup or a channel. The bot must be an administrator in the
    * chat for this to work and must have the appropriate administrator rights. Pass False for all boolean parameters to
    * demote a user. Returns True on success.
    *
    * @param chatId
    *   Unique identifier for the target chat or username of the target channel (in the format &#064;channelusername)
    * @param userId
    *   Unique identifier of the target user
    * @param isAnonymous
    *   Pass True if the administrator's presence in the chat is hidden
    * @param canManageChat
    *   Pass True if the administrator can access the chat event log, get boost list, see hidden supergroup and channel
    *   members, report spam messages, ignore slow mode, and send messages to the chat without paying Telegram Stars.
    *   Implied by any other administrator privilege.
    * @param canDeleteMessages
    *   Pass True if the administrator can delete messages of other users
    * @param canManageVideoChats
    *   Pass True if the administrator can manage video chats
    * @param canRestrictMembers
    *   Pass True if the administrator can restrict, ban or unban chat members, or access supergroup statistics
    * @param canPromoteMembers
    *   Pass True if the administrator can add new administrators with a subset of their own privileges or demote
    *   administrators that they have promoted, directly or indirectly (promoted by administrators that were appointed
    *   by him)
    * @param canChangeInfo
    *   Pass True if the administrator can change chat title, photo and other settings
    * @param canInviteUsers
    *   Pass True if the administrator can invite new users to the chat
    * @param canPostStories
    *   Pass True if the administrator can post stories to the chat
    * @param canEditStories
    *   Pass True if the administrator can edit stories posted by other users, post stories to the chat page, pin chat
    *   stories, and access the chat's story archive
    * @param canDeleteStories
    *   Pass True if the administrator can delete stories posted by other users
    * @param canPostMessages
    *   Pass True if the administrator can post messages in the channel, approve suggested posts, or access channel
    *   statistics; for channels only
    * @param canEditMessages
    *   Pass True if the administrator can edit messages of other users and can pin messages; for channels only
    * @param canPinMessages
    *   Pass True if the administrator can pin messages; for supergroups only
    * @param canManageTopics
    *   Pass True if the user is allowed to create, rename, close, and reopen forum topics; for supergroups only
    */
  def promoteChatMember(
    chatId: ChatId,
    userId: Long,
    isAnonymous: Option[Boolean] = Option.empty,
    canManageChat: Option[Boolean] = Option.empty,
    canDeleteMessages: Option[Boolean] = Option.empty,
    canManageVideoChats: Option[Boolean] = Option.empty,
    canRestrictMembers: Option[Boolean] = Option.empty,
    canPromoteMembers: Option[Boolean] = Option.empty,
    canChangeInfo: Option[Boolean] = Option.empty,
    canInviteUsers: Option[Boolean] = Option.empty,
    canPostStories: Option[Boolean] = Option.empty,
    canEditStories: Option[Boolean] = Option.empty,
    canDeleteStories: Option[Boolean] = Option.empty,
    canPostMessages: Option[Boolean] = Option.empty,
    canEditMessages: Option[Boolean] = Option.empty,
    canPinMessages: Option[Boolean] = Option.empty,
    canManageTopics: Option[Boolean] = Option.empty
  ): Method[Boolean] = {
    val req = PromoteChatMemberReq(
      chatId,
      userId,
      isAnonymous,
      canManageChat,
      canDeleteMessages,
      canManageVideoChats,
      canRestrictMembers,
      canPromoteMembers,
      canChangeInfo,
      canInviteUsers,
      canPostStories,
      canEditStories,
      canDeleteStories,
      canPostMessages,
      canEditMessages,
      canPinMessages,
      canManageTopics
    )
    MethodReq[Boolean]("promoteChatMember", req.asJson)
  }

  /** Marks incoming message as read on behalf of a business account. Requires the can_read_messages business bot right.
    * Returns True on success.
    *
    * @param businessConnectionId
    *   Unique identifier of the business connection on behalf of which to read the message
    * @param chatId
    *   Unique identifier of the chat in which the message was received. The chat must have been active in the last 24
    *   hours.
    * @param messageId
    *   Unique identifier of the message to mark as read
    */
  def readBusinessMessage(businessConnectionId: String, chatId: Long, messageId: Int): Method[Boolean] = {
    val req = ReadBusinessMessageReq(businessConnectionId, chatId, messageId)
    MethodReq[Boolean]("readBusinessMessage", req.asJson)
  }

  /** Refunds a successful payment in Telegram Stars. Returns True on success.
    *
    * @param userId
    *   Identifier of the user whose payment will be refunded
    * @param telegramPaymentChargeId
    *   Telegram payment identifier
    */
  def refundStarPayment(userId: Long, telegramPaymentChargeId: String): Method[Boolean] = {
    val req = RefundStarPaymentReq(userId, telegramPaymentChargeId)
    MethodReq[Boolean]("refundStarPayment", req.asJson)
  }

  /** Removes the current profile photo of a managed business account. Requires the can_edit_profile_photo business bot
    * right. Returns True on success.
    *
    * @param businessConnectionId
    *   Unique identifier of the business connection
    * @param isPublic
    *   Pass True to remove the public photo, which is visible even if the main photo is hidden by the business
    *   account's privacy settings. After the main photo is removed, the previous profile photo (if present) becomes the
    *   main photo.
    */
  def removeBusinessAccountProfilePhoto(
    businessConnectionId: String,
    isPublic: Option[Boolean] = Option.empty
  ): Method[Boolean] = {
    val req = RemoveBusinessAccountProfilePhotoReq(businessConnectionId, isPublic)
    MethodReq[Boolean]("removeBusinessAccountProfilePhoto", req.asJson)
  }

  /** Removes verification from a chat that is currently verified on behalf of the organization represented by the bot.
    * Returns True on success.
    *
    * @param chatId
    *   Unique identifier for the target chat or username of the target channel (in the format &#064;channelusername)
    */
  def removeChatVerification(chatId: ChatId): Method[Boolean] = {
    val req = RemoveChatVerificationReq(chatId)
    MethodReq[Boolean]("removeChatVerification", req.asJson)
  }

  /** Removes verification from a user who is currently verified on behalf of the organization represented by the bot.
    * Returns True on success.
    *
    * @param userId
    *   Unique identifier of the target user
    */
  def removeUserVerification(userId: Long): Method[Boolean] = {
    val req = RemoveUserVerificationReq(userId)
    MethodReq[Boolean]("removeUserVerification", req.asJson)
  }

  /** Use this method to reopen a closed topic in a forum supergroup chat. The bot must be an administrator in the chat
    * for this to work and must have the can_manage_topics administrator rights, unless it is the creator of the topic.
    * Returns True on success.
    *
    * @param chatId
    *   Unique identifier for the target chat or username of the target supergroup (in the format
    *   &#064;supergroupusername)
    * @param messageThreadId
    *   Unique identifier for the target message thread of the forum topic
    */
  def reopenForumTopic(chatId: ChatId, messageThreadId: Int): Method[Boolean] = {
    val req = ReopenForumTopicReq(chatId, messageThreadId)
    MethodReq[Boolean]("reopenForumTopic", req.asJson)
  }

  /** Use this method to reopen a closed 'General' topic in a forum supergroup chat. The bot must be an administrator in
    * the chat for this to work and must have the can_manage_topics administrator rights. The topic will be
    * automatically unhidden if it was hidden. Returns True on success.
    *
    * @param chatId
    *   Unique identifier for the target chat or username of the target supergroup (in the format
    *   &#064;supergroupusername)
    */
  def reopenGeneralForumTopic(chatId: ChatId): Method[Boolean] = {
    val req = ReopenGeneralForumTopicReq(chatId)
    MethodReq[Boolean]("reopenGeneralForumTopic", req.asJson)
  }

  /** Use this method to replace an existing sticker in a sticker set with a new one. The method is equivalent to
    * calling deleteStickerFromSet, then addStickerToSet, then setStickerPositionInSet. Returns True on success.
    *
    * @param userId
    *   User identifier of the sticker set owner
    * @param name
    *   Sticker set name
    * @param oldSticker
    *   File identifier of the replaced sticker
    * @param sticker
    *   A JSON-serialized object with information about the added sticker. If exactly the same sticker had already been
    *   added to the set, then the set remains unchanged.
    */
  def replaceStickerInSet(userId: Long, name: String, oldSticker: String, sticker: InputSticker): Method[Boolean] = {
    val req = ReplaceStickerInSetReq(userId, name, oldSticker, sticker)
    MethodReq[Boolean]("replaceStickerInSet", req.asJson)
  }

  /** Use this method to restrict a user in a supergroup. The bot must be an administrator in the supergroup for this to
    * work and must have the appropriate administrator rights. Pass True for all permissions to lift restrictions from a
    * user. Returns True on success.
    *
    * @param chatId
    *   Unique identifier for the target chat or username of the target supergroup (in the format
    *   &#064;supergroupusername)
    * @param userId
    *   Unique identifier of the target user
    * @param permissions
    *   A JSON-serialized object for new user permissions
    * @param useIndependentChatPermissions
    *   Pass True if chat permissions are set independently. Otherwise, the can_send_other_messages and
    *   can_add_web_page_previews permissions will imply the can_send_messages, can_send_audios, can_send_documents,
    *   can_send_photos, can_send_videos, can_send_video_notes, and can_send_voice_notes permissions; the can_send_polls
    *   permission will imply the can_send_messages permission.
    * @param untilDate
    *   Date when restrictions will be lifted for the user; Unix time. If user is restricted for more than 366 days or
    *   less than 30 seconds from the current time, they are considered to be restricted forever
    */
  def restrictChatMember(
    chatId: ChatId,
    userId: Long,
    permissions: ChatPermissions,
    useIndependentChatPermissions: Option[Boolean] = Option.empty,
    untilDate: Option[Int] = Option.empty
  ): Method[Boolean] = {
    val req = RestrictChatMemberReq(chatId, userId, permissions, useIndependentChatPermissions, untilDate)
    MethodReq[Boolean]("restrictChatMember", req.asJson)
  }

  /** Use this method to revoke an invite link created by the bot. If the primary link is revoked, a new link is
    * automatically generated. The bot must be an administrator in the chat for this to work and must have the
    * appropriate administrator rights. Returns the revoked invite link as ChatInviteLink object.
    *
    * @param chatId
    *   Unique identifier of the target chat or username of the target channel (in the format &#064;channelusername)
    * @param inviteLink
    *   The invite link to revoke
    */
  def revokeChatInviteLink(chatId: ChatId, inviteLink: String): Method[ChatInviteLink] = {
    val req = RevokeChatInviteLinkReq(chatId, inviteLink)
    MethodReq[ChatInviteLink]("revokeChatInviteLink", req.asJson)
  }

  /** Stores a message that can be sent by a user of a Mini App. Returns a PreparedInlineMessage object.
    *
    * @param userId
    *   Unique identifier of the target user that can use the prepared message
    * @param result
    *   A JSON-serialized object describing the message to be sent
    * @param allowUserChats
    *   Pass True if the message can be sent to private chats with users
    * @param allowBotChats
    *   Pass True if the message can be sent to private chats with bots
    * @param allowGroupChats
    *   Pass True if the message can be sent to group and supergroup chats
    * @param allowChannelChats
    *   Pass True if the message can be sent to channel chats
    */
  def savePreparedInlineMessage(
    userId: Long,
    result: InlineQueryResult,
    allowUserChats: Option[Boolean] = Option.empty,
    allowBotChats: Option[Boolean] = Option.empty,
    allowGroupChats: Option[Boolean] = Option.empty,
    allowChannelChats: Option[Boolean] = Option.empty
  ): Method[PreparedInlineMessage] = {
    val req =
      SavePreparedInlineMessageReq(userId, result, allowUserChats, allowBotChats, allowGroupChats, allowChannelChats)
    MethodReq[PreparedInlineMessage]("savePreparedInlineMessage", req.asJson)
  }

  /** Use this method to send animation files (GIF or H.264/MPEG-4 AVC video without sound). On success, the sent
    * Message is returned. Bots can currently send animation files of up to 50 MB in size, this limit may be changed in
    * the future.
    *
    * @param chatId
    *   Unique identifier for the target chat or username of the target channel (in the format &#064;channelusername)
    * @param animation
    *   Animation to send. Pass a file_id as String to send an animation that exists on the Telegram servers
    *   (recommended), pass an HTTP URL as a String for Telegram to get an animation from the Internet, or upload a new
    *   animation using multipart/form-data.
    * @param businessConnectionId
    *   Unique identifier of the business connection on behalf of which the message will be sent
    * @param messageThreadId
    *   Unique identifier for the target message thread (topic) of the forum; for forum supergroups only
    * @param duration
    *   Duration of sent animation in seconds
    * @param width
    *   Animation width
    * @param height
    *   Animation height
    * @param thumbnail
    *   Thumbnail of the file sent; can be ignored if thumbnail generation for the file is supported server-side. The
    *   thumbnail should be in JPEG format and less than 200 kB in size. A thumbnail's width and height should not
    *   exceed 320. Ignored if the file is not uploaded using multipart/form-data. Thumbnails can't be reused and can be
    *   only uploaded as a new file, so you can pass “attach://<file_attach_name>” if the thumbnail was uploaded using
    *   multipart/form-data under <file_attach_name>.
    * @param caption
    *   Animation caption (may also be used when resending animation by file_id), 0-1024 characters after entities
    *   parsing
    * @param parseMode
    *   Mode for parsing entities in the animation caption. See formatting options for more details.
    * @param captionEntities
    *   A JSON-serialized list of special entities that appear in the caption, which can be specified instead of
    *   parse_mode
    * @param showCaptionAboveMedia
    *   Pass True, if the caption must be shown above the message media
    * @param hasSpoiler
    *   Pass True if the animation needs to be covered with a spoiler animation
    * @param disableNotification
    *   Sends the message silently. Users will receive a notification with no sound.
    * @param protectContent
    *   Protects the contents of the sent message from forwarding and saving
    * @param allowPaidBroadcast
    *   Pass True to allow up to 1000 messages per second, ignoring broadcasting limits for a fee of 0.1 Telegram Stars
    *   per message. The relevant Stars will be withdrawn from the bot's balance
    * @param messageEffectId
    *   Unique identifier of the message effect to be added to the message; for private chats only
    * @param replyParameters
    *   Description of the message to reply to
    * @param replyMarkup
    *   Additional interface options. A JSON-serialized object for an inline keyboard, custom reply keyboard,
    *   instructions to remove a reply keyboard or to force a reply from the user
    */
  def sendAnimation(
    chatId: ChatId,
    animation: IFile,
    businessConnectionId: Option[String] = Option.empty,
    messageThreadId: Option[Int] = Option.empty,
    duration: Option[Int] = Option.empty,
    width: Option[Int] = Option.empty,
    height: Option[Int] = Option.empty,
    thumbnail: Option[IFile] = Option.empty,
    caption: Option[String] = Option.empty,
    parseMode: Option[ParseMode] = Option.empty,
    captionEntities: List[MessageEntity] = List.empty,
    showCaptionAboveMedia: Option[Boolean] = Option.empty,
    hasSpoiler: Option[Boolean] = Option.empty,
    disableNotification: Option[Boolean] = Option.empty,
    protectContent: Option[Boolean] = Option.empty,
    allowPaidBroadcast: Option[Boolean] = Option.empty,
    messageEffectId: Option[String] = Option.empty,
    replyParameters: Option[ReplyParameters] = Option.empty,
    replyMarkup: Option[KeyboardMarkup] = Option.empty
  ): Method[Message] = {
    val req = SendAnimationReq(
      chatId,
      animation,
      businessConnectionId,
      messageThreadId,
      duration,
      width,
      height,
      thumbnail,
      caption,
      parseMode,
      captionEntities,
      showCaptionAboveMedia,
      hasSpoiler,
      disableNotification,
      protectContent,
      allowPaidBroadcast,
      messageEffectId,
      replyParameters,
      replyMarkup
    )
    MethodReq[Message](
      "sendAnimation",
      req.asJson,
      Map("animation" -> Option(animation), "thumbnail" -> thumbnail).collect { case (k, Some(v)) => k -> v }
    )
  }

  /** Use this method to send audio files, if you want Telegram clients to display them in the music player. Your audio
    * must be in the .MP3 or .M4A format. On success, the sent Message is returned. Bots can currently send audio files
    * of up to 50 MB in size, this limit may be changed in the future. For sending voice messages, use the sendVoice
    * method instead.
    *
    * @param chatId
    *   Unique identifier for the target chat or username of the target channel (in the format &#064;channelusername)
    * @param audio
    *   Audio file to send. Pass a file_id as String to send an audio file that exists on the Telegram servers
    *   (recommended), pass an HTTP URL as a String for Telegram to get an audio file from the Internet, or upload a new
    *   one using multipart/form-data.
    * @param businessConnectionId
    *   Unique identifier of the business connection on behalf of which the message will be sent
    * @param messageThreadId
    *   Unique identifier for the target message thread (topic) of the forum; for forum supergroups only
    * @param caption
    *   Audio caption, 0-1024 characters after entities parsing
    * @param parseMode
    *   Mode for parsing entities in the audio caption. See formatting options for more details.
    * @param captionEntities
    *   A JSON-serialized list of special entities that appear in the caption, which can be specified instead of
    *   parse_mode
    * @param duration
    *   Duration of the audio in seconds
    * @param performer
    *   Performer
    * @param title
    *   Track name
    * @param thumbnail
    *   Thumbnail of the file sent; can be ignored if thumbnail generation for the file is supported server-side. The
    *   thumbnail should be in JPEG format and less than 200 kB in size. A thumbnail's width and height should not
    *   exceed 320. Ignored if the file is not uploaded using multipart/form-data. Thumbnails can't be reused and can be
    *   only uploaded as a new file, so you can pass “attach://<file_attach_name>” if the thumbnail was uploaded using
    *   multipart/form-data under <file_attach_name>.
    * @param disableNotification
    *   Sends the message silently. Users will receive a notification with no sound.
    * @param protectContent
    *   Protects the contents of the sent message from forwarding and saving
    * @param allowPaidBroadcast
    *   Pass True to allow up to 1000 messages per second, ignoring broadcasting limits for a fee of 0.1 Telegram Stars
    *   per message. The relevant Stars will be withdrawn from the bot's balance
    * @param messageEffectId
    *   Unique identifier of the message effect to be added to the message; for private chats only
    * @param replyParameters
    *   Description of the message to reply to
    * @param replyMarkup
    *   Additional interface options. A JSON-serialized object for an inline keyboard, custom reply keyboard,
    *   instructions to remove a reply keyboard or to force a reply from the user
    */
  def sendAudio(
    chatId: ChatId,
    audio: IFile,
    businessConnectionId: Option[String] = Option.empty,
    messageThreadId: Option[Int] = Option.empty,
    caption: Option[String] = Option.empty,
    parseMode: Option[ParseMode] = Option.empty,
    captionEntities: List[MessageEntity] = List.empty,
    duration: Option[Int] = Option.empty,
    performer: Option[String] = Option.empty,
    title: Option[String] = Option.empty,
    thumbnail: Option[IFile] = Option.empty,
    disableNotification: Option[Boolean] = Option.empty,
    protectContent: Option[Boolean] = Option.empty,
    allowPaidBroadcast: Option[Boolean] = Option.empty,
    messageEffectId: Option[String] = Option.empty,
    replyParameters: Option[ReplyParameters] = Option.empty,
    replyMarkup: Option[KeyboardMarkup] = Option.empty
  ): Method[Message] = {
    val req = SendAudioReq(
      chatId,
      audio,
      businessConnectionId,
      messageThreadId,
      caption,
      parseMode,
      captionEntities,
      duration,
      performer,
      title,
      thumbnail,
      disableNotification,
      protectContent,
      allowPaidBroadcast,
      messageEffectId,
      replyParameters,
      replyMarkup
    )
    MethodReq[Message](
      "sendAudio",
      req.asJson,
      Map("audio" -> Option(audio), "thumbnail" -> thumbnail).collect { case (k, Some(v)) => k -> v }
    )
  }

  /** Use this method when you need to tell the user that something is happening on the bot's side. The status is set
    * for 5 seconds or less (when a message arrives from your bot, Telegram clients clear its typing status). Returns
    * True on success. We only recommend using this method when a response from the bot will take a noticeable amount of
    * time to arrive.
    *
    * @param chatId
    *   Unique identifier for the target chat or username of the target channel (in the format &#064;channelusername)
    * @param action
    *   Type of action to broadcast. Choose one, depending on what the user is about to receive: typing for text
    *   messages, upload_photo for photos, record_video or upload_video for videos, record_voice or upload_voice for
    *   voice notes, upload_document for general files, choose_sticker for stickers, find_location for location data,
    *   record_video_note or upload_video_note for video notes.
    * @param businessConnectionId
    *   Unique identifier of the business connection on behalf of which the action will be sent
    * @param messageThreadId
    *   Unique identifier for the target message thread; for supergroups only
    */
  def sendChatAction(
    chatId: ChatId,
    action: String,
    businessConnectionId: Option[String] = Option.empty,
    messageThreadId: Option[Int] = Option.empty
  ): Method[Boolean] = {
    val req = SendChatActionReq(chatId, action, businessConnectionId, messageThreadId)
    MethodReq[Boolean]("sendChatAction", req.asJson)
  }

  /** Use this method to send a checklist on behalf of a connected business account. On success, the sent Message is
    * returned.
    *
    * @param businessConnectionId
    *   Unique identifier of the business connection on behalf of which the message will be sent
    * @param chatId
    *   Unique identifier for the target chat
    * @param checklist
    *   A JSON-serialized object for the checklist to send
    * @param disableNotification
    *   Sends the message silently. Users will receive a notification with no sound.
    * @param protectContent
    *   Protects the contents of the sent message from forwarding and saving
    * @param messageEffectId
    *   Unique identifier of the message effect to be added to the message
    * @param replyParameters
    *   A JSON-serialized object for description of the message to reply to
    * @param replyMarkup
    *   A JSON-serialized object for an inline keyboard
    */
  def sendChecklist(
    businessConnectionId: String,
    chatId: Long,
    checklist: InputChecklist,
    disableNotification: Option[Boolean] = Option.empty,
    protectContent: Option[Boolean] = Option.empty,
    messageEffectId: Option[String] = Option.empty,
    replyParameters: Option[ReplyParameters] = Option.empty,
    replyMarkup: Option[InlineKeyboardMarkup] = Option.empty
  ): Method[Message] = {
    val req = SendChecklistReq(
      businessConnectionId,
      chatId,
      checklist,
      disableNotification,
      protectContent,
      messageEffectId,
      replyParameters,
      replyMarkup
    )
    MethodReq[Message]("sendChecklist", req.asJson)
  }

  /** Use this method to send phone contacts. On success, the sent Message is returned.
    *
    * @param chatId
    *   Unique identifier for the target chat or username of the target channel (in the format &#064;channelusername)
    * @param phoneNumber
    *   Contact's phone number
    * @param firstName
    *   Contact's first name
    * @param businessConnectionId
    *   Unique identifier of the business connection on behalf of which the message will be sent
    * @param messageThreadId
    *   Unique identifier for the target message thread (topic) of the forum; for forum supergroups only
    * @param lastName
    *   Contact's last name
    * @param vcard
    *   Additional data about the contact in the form of a vCard, 0-2048 bytes
    * @param disableNotification
    *   Sends the message silently. Users will receive a notification with no sound.
    * @param protectContent
    *   Protects the contents of the sent message from forwarding and saving
    * @param allowPaidBroadcast
    *   Pass True to allow up to 1000 messages per second, ignoring broadcasting limits for a fee of 0.1 Telegram Stars
    *   per message. The relevant Stars will be withdrawn from the bot's balance
    * @param messageEffectId
    *   Unique identifier of the message effect to be added to the message; for private chats only
    * @param replyParameters
    *   Description of the message to reply to
    * @param replyMarkup
    *   Additional interface options. A JSON-serialized object for an inline keyboard, custom reply keyboard,
    *   instructions to remove a reply keyboard or to force a reply from the user
    */
  def sendContact(
    chatId: ChatId,
    phoneNumber: String,
    firstName: String,
    businessConnectionId: Option[String] = Option.empty,
    messageThreadId: Option[Int] = Option.empty,
    lastName: Option[String] = Option.empty,
    vcard: Option[String] = Option.empty,
    disableNotification: Option[Boolean] = Option.empty,
    protectContent: Option[Boolean] = Option.empty,
    allowPaidBroadcast: Option[Boolean] = Option.empty,
    messageEffectId: Option[String] = Option.empty,
    replyParameters: Option[ReplyParameters] = Option.empty,
    replyMarkup: Option[KeyboardMarkup] = Option.empty
  ): Method[Message] = {
    val req = SendContactReq(
      chatId,
      phoneNumber,
      firstName,
      businessConnectionId,
      messageThreadId,
      lastName,
      vcard,
      disableNotification,
      protectContent,
      allowPaidBroadcast,
      messageEffectId,
      replyParameters,
      replyMarkup
    )
    MethodReq[Message]("sendContact", req.asJson)
  }

  /** Use this method to send an animated emoji that will display a random value. On success, the sent Message is
    * returned.
    *
    * @param chatId
    *   Unique identifier for the target chat or username of the target channel (in the format &#064;channelusername)
    * @param businessConnectionId
    *   Unique identifier of the business connection on behalf of which the message will be sent
    * @param messageThreadId
    *   Unique identifier for the target message thread (topic) of the forum; for forum supergroups only
    * @param emoji
    *   Emoji on which the dice throw animation is based. Currently, must be one of “🎲”, “🎯”, “🏀”, “⚽”, “🎳”, or
    *   “🎰”. Dice can have values 1-6 for “🎲”, “🎯” and “🎳”, values 1-5 for “🏀” and “⚽”, and values 1-64 for “🎰”.
    *   Defaults to “🎲”
    * @param disableNotification
    *   Sends the message silently. Users will receive a notification with no sound.
    * @param protectContent
    *   Protects the contents of the sent message from forwarding
    * @param allowPaidBroadcast
    *   Pass True to allow up to 1000 messages per second, ignoring broadcasting limits for a fee of 0.1 Telegram Stars
    *   per message. The relevant Stars will be withdrawn from the bot's balance
    * @param messageEffectId
    *   Unique identifier of the message effect to be added to the message; for private chats only
    * @param replyParameters
    *   Description of the message to reply to
    * @param replyMarkup
    *   Additional interface options. A JSON-serialized object for an inline keyboard, custom reply keyboard,
    *   instructions to remove a reply keyboard or to force a reply from the user
    */
  def sendDice(
    chatId: ChatId,
    businessConnectionId: Option[String] = Option.empty,
    messageThreadId: Option[Int] = Option.empty,
    emoji: Option[String] = Option.empty,
    disableNotification: Option[Boolean] = Option.empty,
    protectContent: Option[Boolean] = Option.empty,
    allowPaidBroadcast: Option[Boolean] = Option.empty,
    messageEffectId: Option[String] = Option.empty,
    replyParameters: Option[ReplyParameters] = Option.empty,
    replyMarkup: Option[KeyboardMarkup] = Option.empty
  ): Method[Message] = {
    val req = SendDiceReq(
      chatId,
      businessConnectionId,
      messageThreadId,
      emoji,
      disableNotification,
      protectContent,
      allowPaidBroadcast,
      messageEffectId,
      replyParameters,
      replyMarkup
    )
    MethodReq[Message]("sendDice", req.asJson)
  }

  /** Use this method to send general files. On success, the sent Message is returned. Bots can currently send files of
    * any type of up to 50 MB in size, this limit may be changed in the future.
    *
    * @param chatId
    *   Unique identifier for the target chat or username of the target channel (in the format &#064;channelusername)
    * @param document
    *   File to send. Pass a file_id as String to send a file that exists on the Telegram servers (recommended), pass an
    *   HTTP URL as a String for Telegram to get a file from the Internet, or upload a new one using
    *   multipart/form-data.
    * @param businessConnectionId
    *   Unique identifier of the business connection on behalf of which the message will be sent
    * @param messageThreadId
    *   Unique identifier for the target message thread (topic) of the forum; for forum supergroups only
    * @param thumbnail
    *   Thumbnail of the file sent; can be ignored if thumbnail generation for the file is supported server-side. The
    *   thumbnail should be in JPEG format and less than 200 kB in size. A thumbnail's width and height should not
    *   exceed 320. Ignored if the file is not uploaded using multipart/form-data. Thumbnails can't be reused and can be
    *   only uploaded as a new file, so you can pass “attach://<file_attach_name>” if the thumbnail was uploaded using
    *   multipart/form-data under <file_attach_name>.
    * @param caption
    *   Document caption (may also be used when resending documents by file_id), 0-1024 characters after entities
    *   parsing
    * @param parseMode
    *   Mode for parsing entities in the document caption. See formatting options for more details.
    * @param captionEntities
    *   A JSON-serialized list of special entities that appear in the caption, which can be specified instead of
    *   parse_mode
    * @param disableContentTypeDetection
    *   Disables automatic server-side content type detection for files uploaded using multipart/form-data
    * @param disableNotification
    *   Sends the message silently. Users will receive a notification with no sound.
    * @param protectContent
    *   Protects the contents of the sent message from forwarding and saving
    * @param allowPaidBroadcast
    *   Pass True to allow up to 1000 messages per second, ignoring broadcasting limits for a fee of 0.1 Telegram Stars
    *   per message. The relevant Stars will be withdrawn from the bot's balance
    * @param messageEffectId
    *   Unique identifier of the message effect to be added to the message; for private chats only
    * @param replyParameters
    *   Description of the message to reply to
    * @param replyMarkup
    *   Additional interface options. A JSON-serialized object for an inline keyboard, custom reply keyboard,
    *   instructions to remove a reply keyboard or to force a reply from the user
    */
  def sendDocument(
    chatId: ChatId,
    document: IFile,
    businessConnectionId: Option[String] = Option.empty,
    messageThreadId: Option[Int] = Option.empty,
    thumbnail: Option[IFile] = Option.empty,
    caption: Option[String] = Option.empty,
    parseMode: Option[ParseMode] = Option.empty,
    captionEntities: List[MessageEntity] = List.empty,
    disableContentTypeDetection: Option[Boolean] = Option.empty,
    disableNotification: Option[Boolean] = Option.empty,
    protectContent: Option[Boolean] = Option.empty,
    allowPaidBroadcast: Option[Boolean] = Option.empty,
    messageEffectId: Option[String] = Option.empty,
    replyParameters: Option[ReplyParameters] = Option.empty,
    replyMarkup: Option[KeyboardMarkup] = Option.empty
  ): Method[Message] = {
    val req = SendDocumentReq(
      chatId,
      document,
      businessConnectionId,
      messageThreadId,
      thumbnail,
      caption,
      parseMode,
      captionEntities,
      disableContentTypeDetection,
      disableNotification,
      protectContent,
      allowPaidBroadcast,
      messageEffectId,
      replyParameters,
      replyMarkup
    )
    MethodReq[Message](
      "sendDocument",
      req.asJson,
      Map("document" -> Option(document), "thumbnail" -> thumbnail).collect { case (k, Some(v)) => k -> v }
    )
  }

  /** Use this method to send a game. On success, the sent Message is returned.
    *
    * @param chatId
    *   Unique identifier for the target chat
    * @param gameShortName
    *   Short name of the game, serves as the unique identifier for the game. Set up your games via &#064;BotFather.
    * @param businessConnectionId
    *   Unique identifier of the business connection on behalf of which the message will be sent
    * @param messageThreadId
    *   Unique identifier for the target message thread (topic) of the forum; for forum supergroups only
    * @param disableNotification
    *   Sends the message silently. Users will receive a notification with no sound.
    * @param protectContent
    *   Protects the contents of the sent message from forwarding and saving
    * @param allowPaidBroadcast
    *   Pass True to allow up to 1000 messages per second, ignoring broadcasting limits for a fee of 0.1 Telegram Stars
    *   per message. The relevant Stars will be withdrawn from the bot's balance
    * @param messageEffectId
    *   Unique identifier of the message effect to be added to the message; for private chats only
    * @param replyParameters
    *   Description of the message to reply to
    * @param replyMarkup
    *   A JSON-serialized object for an inline keyboard. If empty, one 'Play game_title' button will be shown. If not
    *   empty, the first button must launch the game.
    */
  def sendGame(
    chatId: Long,
    gameShortName: String,
    businessConnectionId: Option[String] = Option.empty,
    messageThreadId: Option[Int] = Option.empty,
    disableNotification: Option[Boolean] = Option.empty,
    protectContent: Option[Boolean] = Option.empty,
    allowPaidBroadcast: Option[Boolean] = Option.empty,
    messageEffectId: Option[String] = Option.empty,
    replyParameters: Option[ReplyParameters] = Option.empty,
    replyMarkup: Option[InlineKeyboardMarkup] = Option.empty
  ): Method[Message] = {
    val req = SendGameReq(
      chatId,
      gameShortName,
      businessConnectionId,
      messageThreadId,
      disableNotification,
      protectContent,
      allowPaidBroadcast,
      messageEffectId,
      replyParameters,
      replyMarkup
    )
    MethodReq[Message]("sendGame", req.asJson)
  }

  /** Sends a gift to the given user or channel chat. The gift can't be converted to Telegram Stars by the receiver.
    * Returns True on success.
    *
    * @param giftId
    *   Identifier of the gift
    * @param userId
    *   Required if chat_id is not specified. Unique identifier of the target user who will receive the gift.
    * @param chatId
    *   Required if user_id is not specified. Unique identifier for the chat or username of the channel (in the format
    *   &#064;channelusername) that will receive the gift.
    * @param payForUpgrade
    *   Pass True to pay for the gift upgrade from the bot's balance, thereby making the upgrade free for the receiver
    * @param text
    *   Text that will be shown along with the gift; 0-128 characters
    * @param textParseMode
    *   Mode for parsing entities in the text. See formatting options for more details. Entities other than “bold”,
    *   “italic”, “underline”, “strikethrough”, “spoiler”, and “custom_emoji” are ignored.
    * @param textEntities
    *   A JSON-serialized list of special entities that appear in the gift text. It can be specified instead of
    *   text_parse_mode. Entities other than “bold”, “italic”, “underline”, “strikethrough”, “spoiler”, and
    *   “custom_emoji” are ignored.
    */
  def sendGift(
    giftId: String,
    userId: Option[Long] = Option.empty,
    chatId: Option[ChatId] = Option.empty,
    payForUpgrade: Option[Boolean] = Option.empty,
    text: Option[String] = Option.empty,
    textParseMode: Option[ParseMode] = Option.empty,
    textEntities: List[MessageEntity] = List.empty
  ): Method[Boolean] = {
    val req = SendGiftReq(giftId, userId, chatId, payForUpgrade, text, textParseMode, textEntities)
    MethodReq[Boolean]("sendGift", req.asJson)
  }

  /** Use this method to send invoices. On success, the sent Message is returned.
    *
    * @param chatId
    *   Unique identifier for the target chat or username of the target channel (in the format &#064;channelusername)
    * @param title
    *   Product name, 1-32 characters
    * @param description
    *   Product description, 1-255 characters
    * @param payload
    *   Bot-defined invoice payload, 1-128 bytes. This will not be displayed to the user, use it for your internal
    *   processes.
    * @param currency
    *   Three-letter ISO 4217 currency code, see more on currencies. Pass “XTR” for payments in Telegram Stars.
    * @param messageThreadId
    *   Unique identifier for the target message thread (topic) of the forum; for forum supergroups only
    * @param providerToken
    *   Payment provider token, obtained via &#064;BotFather. Pass an empty string for payments in Telegram Stars.
    * @param prices
    *   Price breakdown, a JSON-serialized list of components (e.g. product price, tax, discount, delivery cost,
    *   delivery tax, bonus, etc.). Must contain exactly one item for payments in Telegram Stars.
    * @param maxTipAmount
    *   The maximum accepted amount for tips in the smallest units of the currency (integer, not float/double). For
    *   example, for a maximum tip of US$ 1.45 pass max_tip_amount = 145. See the exp parameter in currencies.json, it
    *   shows the number of digits past the decimal point for each currency (2 for the majority of currencies). Defaults
    *   to 0. Not supported for payments in Telegram Stars.
    * @param suggestedTipAmounts
    *   A JSON-serialized array of suggested amounts of tips in the smallest units of the currency (integer, not
    *   float/double). At most 4 suggested tip amounts can be specified. The suggested tip amounts must be positive,
    *   passed in a strictly increased order and must not exceed max_tip_amount.
    * @param startParameter
    *   Unique deep-linking parameter. If left empty, forwarded copies of the sent message will have a Pay button,
    *   allowing multiple users to pay directly from the forwarded message, using the same invoice. If non-empty,
    *   forwarded copies of the sent message will have a URL button with a deep link to the bot (instead of a Pay
    *   button), with the value used as the start parameter
    * @param providerData
    *   JSON-serialized data about the invoice, which will be shared with the payment provider. A detailed description
    *   of required fields should be provided by the payment provider.
    * @param photoUrl
    *   URL of the product photo for the invoice. Can be a photo of the goods or a marketing image for a service. People
    *   like it better when they see what they are paying for.
    * @param photoSize
    *   Photo size in bytes
    * @param photoWidth
    *   Photo width
    * @param photoHeight
    *   Photo height
    * @param needName
    *   Pass True if you require the user's full name to complete the order. Ignored for payments in Telegram Stars.
    * @param needPhoneNumber
    *   Pass True if you require the user's phone number to complete the order. Ignored for payments in Telegram Stars.
    * @param needEmail
    *   Pass True if you require the user's email address to complete the order. Ignored for payments in Telegram Stars.
    * @param needShippingAddress
    *   Pass True if you require the user's shipping address to complete the order. Ignored for payments in Telegram
    *   Stars.
    * @param sendPhoneNumberToProvider
    *   Pass True if the user's phone number should be sent to the provider. Ignored for payments in Telegram Stars.
    * @param sendEmailToProvider
    *   Pass True if the user's email address should be sent to the provider. Ignored for payments in Telegram Stars.
    * @param isFlexible
    *   Pass True if the final price depends on the shipping method. Ignored for payments in Telegram Stars.
    * @param disableNotification
    *   Sends the message silently. Users will receive a notification with no sound.
    * @param protectContent
    *   Protects the contents of the sent message from forwarding and saving
    * @param allowPaidBroadcast
    *   Pass True to allow up to 1000 messages per second, ignoring broadcasting limits for a fee of 0.1 Telegram Stars
    *   per message. The relevant Stars will be withdrawn from the bot's balance
    * @param messageEffectId
    *   Unique identifier of the message effect to be added to the message; for private chats only
    * @param replyParameters
    *   Description of the message to reply to
    * @param replyMarkup
    *   A JSON-serialized object for an inline keyboard. If empty, one 'Pay total price' button will be shown. If not
    *   empty, the first button must be a Pay button.
    */
  def sendInvoice(
    chatId: ChatId,
    title: String,
    description: String,
    payload: String,
    currency: String,
    messageThreadId: Option[Int] = Option.empty,
    providerToken: Option[String] = Option.empty,
    prices: List[LabeledPrice] = List.empty,
    maxTipAmount: Option[Int] = Option.empty,
    suggestedTipAmounts: List[Int] = List.empty,
    startParameter: Option[String] = Option.empty,
    providerData: Option[String] = Option.empty,
    photoUrl: Option[String] = Option.empty,
    photoSize: Option[Long] = Option.empty,
    photoWidth: Option[Int] = Option.empty,
    photoHeight: Option[Int] = Option.empty,
    needName: Option[Boolean] = Option.empty,
    needPhoneNumber: Option[Boolean] = Option.empty,
    needEmail: Option[Boolean] = Option.empty,
    needShippingAddress: Option[Boolean] = Option.empty,
    sendPhoneNumberToProvider: Option[Boolean] = Option.empty,
    sendEmailToProvider: Option[Boolean] = Option.empty,
    isFlexible: Option[Boolean] = Option.empty,
    disableNotification: Option[Boolean] = Option.empty,
    protectContent: Option[Boolean] = Option.empty,
    allowPaidBroadcast: Option[Boolean] = Option.empty,
    messageEffectId: Option[String] = Option.empty,
    replyParameters: Option[ReplyParameters] = Option.empty,
    replyMarkup: Option[InlineKeyboardMarkup] = Option.empty
  ): Method[Message] = {
    val req = SendInvoiceReq(
      chatId,
      title,
      description,
      payload,
      currency,
      messageThreadId,
      providerToken,
      prices,
      maxTipAmount,
      suggestedTipAmounts,
      startParameter,
      providerData,
      photoUrl,
      photoSize,
      photoWidth,
      photoHeight,
      needName,
      needPhoneNumber,
      needEmail,
      needShippingAddress,
      sendPhoneNumberToProvider,
      sendEmailToProvider,
      isFlexible,
      disableNotification,
      protectContent,
      allowPaidBroadcast,
      messageEffectId,
      replyParameters,
      replyMarkup
    )
    MethodReq[Message]("sendInvoice", req.asJson)
  }

  /** Use this method to send point on the map. On success, the sent Message is returned.
    *
    * @param chatId
    *   Unique identifier for the target chat or username of the target channel (in the format &#064;channelusername)
    * @param latitude
    *   Latitude of the location
    * @param longitude
    *   Longitude of the location
    * @param businessConnectionId
    *   Unique identifier of the business connection on behalf of which the message will be sent
    * @param messageThreadId
    *   Unique identifier for the target message thread (topic) of the forum; for forum supergroups only
    * @param horizontalAccuracy
    *   The radius of uncertainty for the location, measured in meters; 0-1500
    * @param livePeriod
    *   Period in seconds during which the location will be updated (see Live Locations, should be between 60 and 86400,
    *   or 0x7FFFFFFF for live locations that can be edited indefinitely.
    * @param heading
    *   For live locations, a direction in which the user is moving, in degrees. Must be between 1 and 360 if specified.
    * @param proximityAlertRadius
    *   For live locations, a maximum distance for proximity alerts about approaching another chat member, in meters.
    *   Must be between 1 and 100000 if specified.
    * @param disableNotification
    *   Sends the message silently. Users will receive a notification with no sound.
    * @param protectContent
    *   Protects the contents of the sent message from forwarding and saving
    * @param allowPaidBroadcast
    *   Pass True to allow up to 1000 messages per second, ignoring broadcasting limits for a fee of 0.1 Telegram Stars
    *   per message. The relevant Stars will be withdrawn from the bot's balance
    * @param messageEffectId
    *   Unique identifier of the message effect to be added to the message; for private chats only
    * @param replyParameters
    *   Description of the message to reply to
    * @param replyMarkup
    *   Additional interface options. A JSON-serialized object for an inline keyboard, custom reply keyboard,
    *   instructions to remove a reply keyboard or to force a reply from the user
    */
  def sendLocation(
    chatId: ChatId,
    latitude: Float,
    longitude: Float,
    businessConnectionId: Option[String] = Option.empty,
    messageThreadId: Option[Int] = Option.empty,
    horizontalAccuracy: Option[Float] = Option.empty,
    livePeriod: Option[Int] = Option.empty,
    heading: Option[Int] = Option.empty,
    proximityAlertRadius: Option[Int] = Option.empty,
    disableNotification: Option[Boolean] = Option.empty,
    protectContent: Option[Boolean] = Option.empty,
    allowPaidBroadcast: Option[Boolean] = Option.empty,
    messageEffectId: Option[String] = Option.empty,
    replyParameters: Option[ReplyParameters] = Option.empty,
    replyMarkup: Option[KeyboardMarkup] = Option.empty
  ): Method[Message] = {
    val req = SendLocationReq(
      chatId,
      latitude,
      longitude,
      businessConnectionId,
      messageThreadId,
      horizontalAccuracy,
      livePeriod,
      heading,
      proximityAlertRadius,
      disableNotification,
      protectContent,
      allowPaidBroadcast,
      messageEffectId,
      replyParameters,
      replyMarkup
    )
    MethodReq[Message]("sendLocation", req.asJson)
  }

  /** Use this method to send a group of photos, videos, documents or audios as an album. Documents and audio files can
    * be only grouped in an album with messages of the same type. On success, an array of Messages that were sent is
    * returned.
    *
    * @param chatId
    *   Unique identifier for the target chat or username of the target channel (in the format &#064;channelusername)
    * @param businessConnectionId
    *   Unique identifier of the business connection on behalf of which the message will be sent
    * @param messageThreadId
    *   Unique identifier for the target message thread (topic) of the forum; for forum supergroups only
    * @param media
    *   A JSON-serialized array describing messages to be sent, must include 2-10 items
    * @param disableNotification
    *   Sends messages silently. Users will receive a notification with no sound.
    * @param protectContent
    *   Protects the contents of the sent messages from forwarding and saving
    * @param allowPaidBroadcast
    *   Pass True to allow up to 1000 messages per second, ignoring broadcasting limits for a fee of 0.1 Telegram Stars
    *   per message. The relevant Stars will be withdrawn from the bot's balance
    * @param messageEffectId
    *   Unique identifier of the message effect to be added to the message; for private chats only
    * @param replyParameters
    *   Description of the message to reply to
    */
  def sendMediaGroup(
    chatId: ChatId,
    businessConnectionId: Option[String] = Option.empty,
    messageThreadId: Option[Int] = Option.empty,
    media: List[InputMedia] = List.empty,
    disableNotification: Option[Boolean] = Option.empty,
    protectContent: Option[Boolean] = Option.empty,
    allowPaidBroadcast: Option[Boolean] = Option.empty,
    messageEffectId: Option[String] = Option.empty,
    replyParameters: Option[ReplyParameters] = Option.empty
  ): Method[List[Message]] = {
    val req = SendMediaGroupReq(
      chatId,
      businessConnectionId,
      messageThreadId,
      media,
      disableNotification,
      protectContent,
      allowPaidBroadcast,
      messageEffectId,
      replyParameters
    )
    MethodReq[List[Message]]("sendMediaGroup", req.asJson)
  }

  /** Use this method to send text messages. On success, the sent Message is returned.
    *
    * @param chatId
    *   Unique identifier for the target chat or username of the target channel (in the format &#064;channelusername)
    * @param text
    *   Text of the message to be sent, 1-4096 characters after entities parsing
    * @param businessConnectionId
    *   Unique identifier of the business connection on behalf of which the message will be sent
    * @param messageThreadId
    *   Unique identifier for the target message thread (topic) of the forum; for forum supergroups only
    * @param parseMode
    *   Mode for parsing entities in the message text. See formatting options for more details.
    * @param entities
    *   A JSON-serialized list of special entities that appear in message text, which can be specified instead of
    *   parse_mode
    * @param linkPreviewOptions
    *   Link preview generation options for the message
    * @param disableNotification
    *   Sends the message silently. Users will receive a notification with no sound.
    * @param protectContent
    *   Protects the contents of the sent message from forwarding and saving
    * @param allowPaidBroadcast
    *   Pass True to allow up to 1000 messages per second, ignoring broadcasting limits for a fee of 0.1 Telegram Stars
    *   per message. The relevant Stars will be withdrawn from the bot's balance
    * @param messageEffectId
    *   Unique identifier of the message effect to be added to the message; for private chats only
    * @param replyParameters
    *   Description of the message to reply to
    * @param replyMarkup
    *   Additional interface options. A JSON-serialized object for an inline keyboard, custom reply keyboard,
    *   instructions to remove a reply keyboard or to force a reply from the user
    */
  def sendMessage(
    chatId: ChatId,
    text: String,
    businessConnectionId: Option[String] = Option.empty,
    messageThreadId: Option[Int] = Option.empty,
    parseMode: Option[ParseMode] = Option.empty,
    entities: List[MessageEntity] = List.empty,
    linkPreviewOptions: Option[LinkPreviewOptions] = Option.empty,
    disableNotification: Option[Boolean] = Option.empty,
    protectContent: Option[Boolean] = Option.empty,
    allowPaidBroadcast: Option[Boolean] = Option.empty,
    messageEffectId: Option[String] = Option.empty,
    replyParameters: Option[ReplyParameters] = Option.empty,
    replyMarkup: Option[KeyboardMarkup] = Option.empty
  ): Method[Message] = {
    val req = SendMessageReq(
      chatId,
      text,
      businessConnectionId,
      messageThreadId,
      parseMode,
      entities,
      linkPreviewOptions,
      disableNotification,
      protectContent,
      allowPaidBroadcast,
      messageEffectId,
      replyParameters,
      replyMarkup
    )
    MethodReq[Message]("sendMessage", req.asJson)
  }

  /** Use this method to send paid media. On success, the sent Message is returned.
    *
    * @param chatId
    *   Unique identifier for the target chat or username of the target channel (in the format &#064;channelusername).
    *   If the chat is a channel, all Telegram Star proceeds from this media will be credited to the chat's balance.
    *   Otherwise, they will be credited to the bot's balance.
    * @param starCount
    *   The number of Telegram Stars that must be paid to buy access to the media; 1-10000
    * @param businessConnectionId
    *   Unique identifier of the business connection on behalf of which the message will be sent
    * @param media
    *   A JSON-serialized array describing the media to be sent; up to 10 items
    * @param payload
    *   Bot-defined paid media payload, 0-128 bytes. This will not be displayed to the user, use it for your internal
    *   processes.
    * @param caption
    *   Media caption, 0-1024 characters after entities parsing
    * @param parseMode
    *   Mode for parsing entities in the media caption. See formatting options for more details.
    * @param captionEntities
    *   A JSON-serialized list of special entities that appear in the caption, which can be specified instead of
    *   parse_mode
    * @param showCaptionAboveMedia
    *   Pass True, if the caption must be shown above the message media
    * @param disableNotification
    *   Sends the message silently. Users will receive a notification with no sound.
    * @param protectContent
    *   Protects the contents of the sent message from forwarding and saving
    * @param allowPaidBroadcast
    *   Pass True to allow up to 1000 messages per second, ignoring broadcasting limits for a fee of 0.1 Telegram Stars
    *   per message. The relevant Stars will be withdrawn from the bot's balance
    * @param replyParameters
    *   Description of the message to reply to
    * @param replyMarkup
    *   Additional interface options. A JSON-serialized object for an inline keyboard, custom reply keyboard,
    *   instructions to remove a reply keyboard or to force a reply from the user
    */
  def sendPaidMedia(
    chatId: ChatId,
    starCount: Int,
    businessConnectionId: Option[String] = Option.empty,
    media: List[InputPaidMedia] = List.empty,
    payload: Option[String] = Option.empty,
    caption: Option[String] = Option.empty,
    parseMode: Option[ParseMode] = Option.empty,
    captionEntities: List[MessageEntity] = List.empty,
    showCaptionAboveMedia: Option[Boolean] = Option.empty,
    disableNotification: Option[Boolean] = Option.empty,
    protectContent: Option[Boolean] = Option.empty,
    allowPaidBroadcast: Option[Boolean] = Option.empty,
    replyParameters: Option[ReplyParameters] = Option.empty,
    replyMarkup: Option[KeyboardMarkup] = Option.empty
  ): Method[Message] = {
    val req = SendPaidMediaReq(
      chatId,
      starCount,
      businessConnectionId,
      media,
      payload,
      caption,
      parseMode,
      captionEntities,
      showCaptionAboveMedia,
      disableNotification,
      protectContent,
      allowPaidBroadcast,
      replyParameters,
      replyMarkup
    )
    MethodReq[Message]("sendPaidMedia", req.asJson)
  }

  /** Use this method to send photos. On success, the sent Message is returned.
    *
    * @param chatId
    *   Unique identifier for the target chat or username of the target channel (in the format &#064;channelusername)
    * @param photo
    *   Photo to send. Pass a file_id as String to send a photo that exists on the Telegram servers (recommended), pass
    *   an HTTP URL as a String for Telegram to get a photo from the Internet, or upload a new photo using
    *   multipart/form-data. The photo must be at most 10 MB in size. The photo's width and height must not exceed 10000
    *   in total. Width and height ratio must be at most 20.
    * @param businessConnectionId
    *   Unique identifier of the business connection on behalf of which the message will be sent
    * @param messageThreadId
    *   Unique identifier for the target message thread (topic) of the forum; for forum supergroups only
    * @param caption
    *   Photo caption (may also be used when resending photos by file_id), 0-1024 characters after entities parsing
    * @param parseMode
    *   Mode for parsing entities in the photo caption. See formatting options for more details.
    * @param captionEntities
    *   A JSON-serialized list of special entities that appear in the caption, which can be specified instead of
    *   parse_mode
    * @param showCaptionAboveMedia
    *   Pass True, if the caption must be shown above the message media
    * @param hasSpoiler
    *   Pass True if the photo needs to be covered with a spoiler animation
    * @param disableNotification
    *   Sends the message silently. Users will receive a notification with no sound.
    * @param protectContent
    *   Protects the contents of the sent message from forwarding and saving
    * @param allowPaidBroadcast
    *   Pass True to allow up to 1000 messages per second, ignoring broadcasting limits for a fee of 0.1 Telegram Stars
    *   per message. The relevant Stars will be withdrawn from the bot's balance
    * @param messageEffectId
    *   Unique identifier of the message effect to be added to the message; for private chats only
    * @param replyParameters
    *   Description of the message to reply to
    * @param replyMarkup
    *   Additional interface options. A JSON-serialized object for an inline keyboard, custom reply keyboard,
    *   instructions to remove a reply keyboard or to force a reply from the user
    */
  def sendPhoto(
    chatId: ChatId,
    photo: IFile,
    businessConnectionId: Option[String] = Option.empty,
    messageThreadId: Option[Int] = Option.empty,
    caption: Option[String] = Option.empty,
    parseMode: Option[ParseMode] = Option.empty,
    captionEntities: List[MessageEntity] = List.empty,
    showCaptionAboveMedia: Option[Boolean] = Option.empty,
    hasSpoiler: Option[Boolean] = Option.empty,
    disableNotification: Option[Boolean] = Option.empty,
    protectContent: Option[Boolean] = Option.empty,
    allowPaidBroadcast: Option[Boolean] = Option.empty,
    messageEffectId: Option[String] = Option.empty,
    replyParameters: Option[ReplyParameters] = Option.empty,
    replyMarkup: Option[KeyboardMarkup] = Option.empty
  ): Method[Message] = {
    val req = SendPhotoReq(
      chatId,
      photo,
      businessConnectionId,
      messageThreadId,
      caption,
      parseMode,
      captionEntities,
      showCaptionAboveMedia,
      hasSpoiler,
      disableNotification,
      protectContent,
      allowPaidBroadcast,
      messageEffectId,
      replyParameters,
      replyMarkup
    )
    MethodReq[Message]("sendPhoto", req.asJson, Map("photo" -> Option(photo)).collect { case (k, Some(v)) => k -> v })
  }

  /** Use this method to send a native poll. On success, the sent Message is returned.
    *
    * @param chatId
    *   Unique identifier for the target chat or username of the target channel (in the format &#064;channelusername)
    * @param question
    *   Poll question, 1-300 characters
    * @param businessConnectionId
    *   Unique identifier of the business connection on behalf of which the message will be sent
    * @param messageThreadId
    *   Unique identifier for the target message thread (topic) of the forum; for forum supergroups only
    * @param questionParseMode
    *   Mode for parsing entities in the question. See formatting options for more details. Currently, only custom emoji
    *   entities are allowed
    * @param questionEntities
    *   A JSON-serialized list of special entities that appear in the poll question. It can be specified instead of
    *   question_parse_mode
    * @param options
    *   A JSON-serialized list of 2-12 answer options
    * @param isAnonymous
    *   True, if the poll needs to be anonymous, defaults to True
    * @param type
    *   Poll type, “quiz” or “regular”, defaults to “regular”
    * @param allowsMultipleAnswers
    *   True, if the poll allows multiple answers, ignored for polls in quiz mode, defaults to False
    * @param correctOptionId
    *   0-based identifier of the correct answer option, required for polls in quiz mode
    * @param explanation
    *   Text that is shown when a user chooses an incorrect answer or taps on the lamp icon in a quiz-style poll, 0-200
    *   characters with at most 2 line feeds after entities parsing
    * @param explanationParseMode
    *   Mode for parsing entities in the explanation. See formatting options for more details.
    * @param explanationEntities
    *   A JSON-serialized list of special entities that appear in the poll explanation. It can be specified instead of
    *   explanation_parse_mode
    * @param openPeriod
    *   Amount of time in seconds the poll will be active after creation, 5-600. Can't be used together with close_date.
    * @param closeDate
    *   Point in time (Unix timestamp) when the poll will be automatically closed. Must be at least 5 and no more than
    *   600 seconds in the future. Can't be used together with open_period.
    * @param isClosed
    *   Pass True if the poll needs to be immediately closed. This can be useful for poll preview.
    * @param disableNotification
    *   Sends the message silently. Users will receive a notification with no sound.
    * @param protectContent
    *   Protects the contents of the sent message from forwarding and saving
    * @param allowPaidBroadcast
    *   Pass True to allow up to 1000 messages per second, ignoring broadcasting limits for a fee of 0.1 Telegram Stars
    *   per message. The relevant Stars will be withdrawn from the bot's balance
    * @param messageEffectId
    *   Unique identifier of the message effect to be added to the message; for private chats only
    * @param replyParameters
    *   Description of the message to reply to
    * @param replyMarkup
    *   Additional interface options. A JSON-serialized object for an inline keyboard, custom reply keyboard,
    *   instructions to remove a reply keyboard or to force a reply from the user
    */
  def sendPoll(
    chatId: ChatId,
    question: String,
    businessConnectionId: Option[String] = Option.empty,
    messageThreadId: Option[Int] = Option.empty,
    questionParseMode: Option[String] = Option.empty,
    questionEntities: List[MessageEntity] = List.empty,
    options: List[InputPollOption] = List.empty,
    isAnonymous: Option[Boolean] = Option.empty,
    `type`: Option[String] = Option.empty,
    allowsMultipleAnswers: Option[Boolean] = Option.empty,
    correctOptionId: Option[Int] = Option.empty,
    explanation: Option[String] = Option.empty,
    explanationParseMode: Option[ParseMode] = Option.empty,
    explanationEntities: List[MessageEntity] = List.empty,
    openPeriod: Option[Int] = Option.empty,
    closeDate: Option[Int] = Option.empty,
    isClosed: Option[Boolean] = Option.empty,
    disableNotification: Option[Boolean] = Option.empty,
    protectContent: Option[Boolean] = Option.empty,
    allowPaidBroadcast: Option[Boolean] = Option.empty,
    messageEffectId: Option[String] = Option.empty,
    replyParameters: Option[ReplyParameters] = Option.empty,
    replyMarkup: Option[KeyboardMarkup] = Option.empty
  ): Method[Message] = {
    val req = SendPollReq(
      chatId,
      question,
      businessConnectionId,
      messageThreadId,
      questionParseMode,
      questionEntities,
      options,
      isAnonymous,
      `type`,
      allowsMultipleAnswers,
      correctOptionId,
      explanation,
      explanationParseMode,
      explanationEntities,
      openPeriod,
      closeDate,
      isClosed,
      disableNotification,
      protectContent,
      allowPaidBroadcast,
      messageEffectId,
      replyParameters,
      replyMarkup
    )
    MethodReq[Message]("sendPoll", req.asJson)
  }

  /** Use this method to send static .WEBP, animated .TGS, or video .WEBM stickers. On success, the sent Message is
    * returned.
    *
    * @param chatId
    *   Unique identifier for the target chat or username of the target channel (in the format &#064;channelusername)
    * @param sticker
    *   Sticker to send. Pass a file_id as String to send a file that exists on the Telegram servers (recommended), pass
    *   an HTTP URL as a String for Telegram to get a .WEBP sticker from the Internet, or upload a new .WEBP, .TGS, or
    *   .WEBM sticker using multipart/form-data. Video and animated stickers can't be sent via an HTTP URL.
    * @param businessConnectionId
    *   Unique identifier of the business connection on behalf of which the message will be sent
    * @param messageThreadId
    *   Unique identifier for the target message thread (topic) of the forum; for forum supergroups only
    * @param emoji
    *   Emoji associated with the sticker; only for just uploaded stickers
    * @param disableNotification
    *   Sends the message silently. Users will receive a notification with no sound.
    * @param protectContent
    *   Protects the contents of the sent message from forwarding and saving
    * @param allowPaidBroadcast
    *   Pass True to allow up to 1000 messages per second, ignoring broadcasting limits for a fee of 0.1 Telegram Stars
    *   per message. The relevant Stars will be withdrawn from the bot's balance
    * @param messageEffectId
    *   Unique identifier of the message effect to be added to the message; for private chats only
    * @param replyParameters
    *   Description of the message to reply to
    * @param replyMarkup
    *   Additional interface options. A JSON-serialized object for an inline keyboard, custom reply keyboard,
    *   instructions to remove a reply keyboard or to force a reply from the user
    */
  def sendSticker(
    chatId: ChatId,
    sticker: IFile,
    businessConnectionId: Option[String] = Option.empty,
    messageThreadId: Option[Int] = Option.empty,
    emoji: Option[String] = Option.empty,
    disableNotification: Option[Boolean] = Option.empty,
    protectContent: Option[Boolean] = Option.empty,
    allowPaidBroadcast: Option[Boolean] = Option.empty,
    messageEffectId: Option[String] = Option.empty,
    replyParameters: Option[ReplyParameters] = Option.empty,
    replyMarkup: Option[KeyboardMarkup] = Option.empty
  ): Method[Message] = {
    val req = SendStickerReq(
      chatId,
      sticker,
      businessConnectionId,
      messageThreadId,
      emoji,
      disableNotification,
      protectContent,
      allowPaidBroadcast,
      messageEffectId,
      replyParameters,
      replyMarkup
    )
    MethodReq[Message](
      "sendSticker",
      req.asJson,
      Map("sticker" -> Option(sticker)).collect { case (k, Some(v)) => k -> v }
    )
  }

  /** Use this method to send information about a venue. On success, the sent Message is returned.
    *
    * @param chatId
    *   Unique identifier for the target chat or username of the target channel (in the format &#064;channelusername)
    * @param latitude
    *   Latitude of the venue
    * @param longitude
    *   Longitude of the venue
    * @param title
    *   Name of the venue
    * @param address
    *   Address of the venue
    * @param businessConnectionId
    *   Unique identifier of the business connection on behalf of which the message will be sent
    * @param messageThreadId
    *   Unique identifier for the target message thread (topic) of the forum; for forum supergroups only
    * @param foursquareId
    *   Foursquare identifier of the venue
    * @param foursquareType
    *   Foursquare type of the venue, if known. (For example, “arts_entertainment/default”,
    *   “arts_entertainment/aquarium” or “food/icecream”.)
    * @param googlePlaceId
    *   Google Places identifier of the venue
    * @param googlePlaceType
    *   Google Places type of the venue. (See supported types.)
    * @param disableNotification
    *   Sends the message silently. Users will receive a notification with no sound.
    * @param protectContent
    *   Protects the contents of the sent message from forwarding and saving
    * @param allowPaidBroadcast
    *   Pass True to allow up to 1000 messages per second, ignoring broadcasting limits for a fee of 0.1 Telegram Stars
    *   per message. The relevant Stars will be withdrawn from the bot's balance
    * @param messageEffectId
    *   Unique identifier of the message effect to be added to the message; for private chats only
    * @param replyParameters
    *   Description of the message to reply to
    * @param replyMarkup
    *   Additional interface options. A JSON-serialized object for an inline keyboard, custom reply keyboard,
    *   instructions to remove a reply keyboard or to force a reply from the user
    */
  def sendVenue(
    chatId: ChatId,
    latitude: Float,
    longitude: Float,
    title: String,
    address: String,
    businessConnectionId: Option[String] = Option.empty,
    messageThreadId: Option[Int] = Option.empty,
    foursquareId: Option[String] = Option.empty,
    foursquareType: Option[String] = Option.empty,
    googlePlaceId: Option[String] = Option.empty,
    googlePlaceType: Option[String] = Option.empty,
    disableNotification: Option[Boolean] = Option.empty,
    protectContent: Option[Boolean] = Option.empty,
    allowPaidBroadcast: Option[Boolean] = Option.empty,
    messageEffectId: Option[String] = Option.empty,
    replyParameters: Option[ReplyParameters] = Option.empty,
    replyMarkup: Option[KeyboardMarkup] = Option.empty
  ): Method[Message] = {
    val req = SendVenueReq(
      chatId,
      latitude,
      longitude,
      title,
      address,
      businessConnectionId,
      messageThreadId,
      foursquareId,
      foursquareType,
      googlePlaceId,
      googlePlaceType,
      disableNotification,
      protectContent,
      allowPaidBroadcast,
      messageEffectId,
      replyParameters,
      replyMarkup
    )
    MethodReq[Message]("sendVenue", req.asJson)
  }

  /** Use this method to send video files, Telegram clients support MPEG4 videos (other formats may be sent as
    * Document). On success, the sent Message is returned. Bots can currently send video files of up to 50 MB in size,
    * this limit may be changed in the future.
    *
    * @param chatId
    *   Unique identifier for the target chat or username of the target channel (in the format &#064;channelusername)
    * @param video
    *   Video to send. Pass a file_id as String to send a video that exists on the Telegram servers (recommended), pass
    *   an HTTP URL as a String for Telegram to get a video from the Internet, or upload a new video using
    *   multipart/form-data.
    * @param businessConnectionId
    *   Unique identifier of the business connection on behalf of which the message will be sent
    * @param messageThreadId
    *   Unique identifier for the target message thread (topic) of the forum; for forum supergroups only
    * @param duration
    *   Duration of sent video in seconds
    * @param width
    *   Video width
    * @param height
    *   Video height
    * @param thumbnail
    *   Thumbnail of the file sent; can be ignored if thumbnail generation for the file is supported server-side. The
    *   thumbnail should be in JPEG format and less than 200 kB in size. A thumbnail's width and height should not
    *   exceed 320. Ignored if the file is not uploaded using multipart/form-data. Thumbnails can't be reused and can be
    *   only uploaded as a new file, so you can pass “attach://<file_attach_name>” if the thumbnail was uploaded using
    *   multipart/form-data under <file_attach_name>.
    * @param cover
    *   Cover for the video in the message. Pass a file_id to send a file that exists on the Telegram servers
    *   (recommended), pass an HTTP URL for Telegram to get a file from the Internet, or pass
    *   “attach://<file_attach_name>” to upload a new one using multipart/form-data under <file_attach_name> name.
    * @param startTimestamp
    *   Start timestamp for the video in the message
    * @param caption
    *   Video caption (may also be used when resending videos by file_id), 0-1024 characters after entities parsing
    * @param parseMode
    *   Mode for parsing entities in the video caption. See formatting options for more details.
    * @param captionEntities
    *   A JSON-serialized list of special entities that appear in the caption, which can be specified instead of
    *   parse_mode
    * @param showCaptionAboveMedia
    *   Pass True, if the caption must be shown above the message media
    * @param hasSpoiler
    *   Pass True if the video needs to be covered with a spoiler animation
    * @param supportsStreaming
    *   Pass True if the uploaded video is suitable for streaming
    * @param disableNotification
    *   Sends the message silently. Users will receive a notification with no sound.
    * @param protectContent
    *   Protects the contents of the sent message from forwarding and saving
    * @param allowPaidBroadcast
    *   Pass True to allow up to 1000 messages per second, ignoring broadcasting limits for a fee of 0.1 Telegram Stars
    *   per message. The relevant Stars will be withdrawn from the bot's balance
    * @param messageEffectId
    *   Unique identifier of the message effect to be added to the message; for private chats only
    * @param replyParameters
    *   Description of the message to reply to
    * @param replyMarkup
    *   Additional interface options. A JSON-serialized object for an inline keyboard, custom reply keyboard,
    *   instructions to remove a reply keyboard or to force a reply from the user
    */
  def sendVideo(
    chatId: ChatId,
    video: IFile,
    businessConnectionId: Option[String] = Option.empty,
    messageThreadId: Option[Int] = Option.empty,
    duration: Option[Int] = Option.empty,
    width: Option[Int] = Option.empty,
    height: Option[Int] = Option.empty,
    thumbnail: Option[IFile] = Option.empty,
    cover: Option[IFile] = Option.empty,
    startTimestamp: Option[Int] = Option.empty,
    caption: Option[String] = Option.empty,
    parseMode: Option[ParseMode] = Option.empty,
    captionEntities: List[MessageEntity] = List.empty,
    showCaptionAboveMedia: Option[Boolean] = Option.empty,
    hasSpoiler: Option[Boolean] = Option.empty,
    supportsStreaming: Option[Boolean] = Option.empty,
    disableNotification: Option[Boolean] = Option.empty,
    protectContent: Option[Boolean] = Option.empty,
    allowPaidBroadcast: Option[Boolean] = Option.empty,
    messageEffectId: Option[String] = Option.empty,
    replyParameters: Option[ReplyParameters] = Option.empty,
    replyMarkup: Option[KeyboardMarkup] = Option.empty
  ): Method[Message] = {
    val req = SendVideoReq(
      chatId,
      video,
      businessConnectionId,
      messageThreadId,
      duration,
      width,
      height,
      thumbnail,
      cover,
      startTimestamp,
      caption,
      parseMode,
      captionEntities,
      showCaptionAboveMedia,
      hasSpoiler,
      supportsStreaming,
      disableNotification,
      protectContent,
      allowPaidBroadcast,
      messageEffectId,
      replyParameters,
      replyMarkup
    )
    MethodReq[Message](
      "sendVideo",
      req.asJson,
      Map("video" -> Option(video), "thumbnail" -> thumbnail, "cover" -> cover).collect { case (k, Some(v)) => k -> v }
    )
  }

  /** As of v.4.0, Telegram clients support rounded square MPEG4 videos of up to 1 minute long. Use this method to send
    * video messages. On success, the sent Message is returned.
    *
    * @param chatId
    *   Unique identifier for the target chat or username of the target channel (in the format &#064;channelusername)
    * @param videoNote
    *   Video note to send. Pass a file_id as String to send a video note that exists on the Telegram servers
    *   (recommended) or upload a new video using multipart/form-data. Sending video notes by a URL is currently
    *   unsupported
    * @param businessConnectionId
    *   Unique identifier of the business connection on behalf of which the message will be sent
    * @param messageThreadId
    *   Unique identifier for the target message thread (topic) of the forum; for forum supergroups only
    * @param duration
    *   Duration of sent video in seconds
    * @param length
    *   Video width and height, i.e. diameter of the video message
    * @param thumbnail
    *   Thumbnail of the file sent; can be ignored if thumbnail generation for the file is supported server-side. The
    *   thumbnail should be in JPEG format and less than 200 kB in size. A thumbnail's width and height should not
    *   exceed 320. Ignored if the file is not uploaded using multipart/form-data. Thumbnails can't be reused and can be
    *   only uploaded as a new file, so you can pass “attach://<file_attach_name>” if the thumbnail was uploaded using
    *   multipart/form-data under <file_attach_name>.
    * @param disableNotification
    *   Sends the message silently. Users will receive a notification with no sound.
    * @param protectContent
    *   Protects the contents of the sent message from forwarding and saving
    * @param allowPaidBroadcast
    *   Pass True to allow up to 1000 messages per second, ignoring broadcasting limits for a fee of 0.1 Telegram Stars
    *   per message. The relevant Stars will be withdrawn from the bot's balance
    * @param messageEffectId
    *   Unique identifier of the message effect to be added to the message; for private chats only
    * @param replyParameters
    *   Description of the message to reply to
    * @param replyMarkup
    *   Additional interface options. A JSON-serialized object for an inline keyboard, custom reply keyboard,
    *   instructions to remove a reply keyboard or to force a reply from the user
    */
  def sendVideoNote(
    chatId: ChatId,
    videoNote: IFile,
    businessConnectionId: Option[String] = Option.empty,
    messageThreadId: Option[Int] = Option.empty,
    duration: Option[Int] = Option.empty,
    length: Option[Int] = Option.empty,
    thumbnail: Option[IFile] = Option.empty,
    disableNotification: Option[Boolean] = Option.empty,
    protectContent: Option[Boolean] = Option.empty,
    allowPaidBroadcast: Option[Boolean] = Option.empty,
    messageEffectId: Option[String] = Option.empty,
    replyParameters: Option[ReplyParameters] = Option.empty,
    replyMarkup: Option[KeyboardMarkup] = Option.empty
  ): Method[Message] = {
    val req = SendVideoNoteReq(
      chatId,
      videoNote,
      businessConnectionId,
      messageThreadId,
      duration,
      length,
      thumbnail,
      disableNotification,
      protectContent,
      allowPaidBroadcast,
      messageEffectId,
      replyParameters,
      replyMarkup
    )
    MethodReq[Message](
      "sendVideoNote",
      req.asJson,
      Map("video_note" -> Option(videoNote), "thumbnail" -> thumbnail).collect { case (k, Some(v)) => k -> v }
    )
  }

  /** Use this method to send audio files, if you want Telegram clients to display the file as a playable voice message.
    * For this to work, your audio must be in an .OGG file encoded with OPUS, or in .MP3 format, or in .M4A format
    * (other formats may be sent as Audio or Document). On success, the sent Message is returned. Bots can currently
    * send voice messages of up to 50 MB in size, this limit may be changed in the future.
    *
    * @param chatId
    *   Unique identifier for the target chat or username of the target channel (in the format &#064;channelusername)
    * @param voice
    *   Audio file to send. Pass a file_id as String to send a file that exists on the Telegram servers (recommended),
    *   pass an HTTP URL as a String for Telegram to get a file from the Internet, or upload a new one using
    *   multipart/form-data.
    * @param businessConnectionId
    *   Unique identifier of the business connection on behalf of which the message will be sent
    * @param messageThreadId
    *   Unique identifier for the target message thread (topic) of the forum; for forum supergroups only
    * @param caption
    *   Voice message caption, 0-1024 characters after entities parsing
    * @param parseMode
    *   Mode for parsing entities in the voice message caption. See formatting options for more details.
    * @param captionEntities
    *   A JSON-serialized list of special entities that appear in the caption, which can be specified instead of
    *   parse_mode
    * @param duration
    *   Duration of the voice message in seconds
    * @param disableNotification
    *   Sends the message silently. Users will receive a notification with no sound.
    * @param protectContent
    *   Protects the contents of the sent message from forwarding and saving
    * @param allowPaidBroadcast
    *   Pass True to allow up to 1000 messages per second, ignoring broadcasting limits for a fee of 0.1 Telegram Stars
    *   per message. The relevant Stars will be withdrawn from the bot's balance
    * @param messageEffectId
    *   Unique identifier of the message effect to be added to the message; for private chats only
    * @param replyParameters
    *   Description of the message to reply to
    * @param replyMarkup
    *   Additional interface options. A JSON-serialized object for an inline keyboard, custom reply keyboard,
    *   instructions to remove a reply keyboard or to force a reply from the user
    */
  def sendVoice(
    chatId: ChatId,
    voice: IFile,
    businessConnectionId: Option[String] = Option.empty,
    messageThreadId: Option[Int] = Option.empty,
    caption: Option[String] = Option.empty,
    parseMode: Option[ParseMode] = Option.empty,
    captionEntities: List[MessageEntity] = List.empty,
    duration: Option[Int] = Option.empty,
    disableNotification: Option[Boolean] = Option.empty,
    protectContent: Option[Boolean] = Option.empty,
    allowPaidBroadcast: Option[Boolean] = Option.empty,
    messageEffectId: Option[String] = Option.empty,
    replyParameters: Option[ReplyParameters] = Option.empty,
    replyMarkup: Option[KeyboardMarkup] = Option.empty
  ): Method[Message] = {
    val req = SendVoiceReq(
      chatId,
      voice,
      businessConnectionId,
      messageThreadId,
      caption,
      parseMode,
      captionEntities,
      duration,
      disableNotification,
      protectContent,
      allowPaidBroadcast,
      messageEffectId,
      replyParameters,
      replyMarkup
    )
    MethodReq[Message]("sendVoice", req.asJson, Map("voice" -> Option(voice)).collect { case (k, Some(v)) => k -> v })
  }

  /** Changes the bio of a managed business account. Requires the can_change_bio business bot right. Returns True on
    * success.
    *
    * @param businessConnectionId
    *   Unique identifier of the business connection
    * @param bio
    *   The new value of the bio for the business account; 0-140 characters
    */
  def setBusinessAccountBio(businessConnectionId: String, bio: Option[String] = Option.empty): Method[Boolean] = {
    val req = SetBusinessAccountBioReq(businessConnectionId, bio)
    MethodReq[Boolean]("setBusinessAccountBio", req.asJson)
  }

  /** Changes the privacy settings pertaining to incoming gifts in a managed business account. Requires the
    * can_change_gift_settings business bot right. Returns True on success.
    *
    * @param businessConnectionId
    *   Unique identifier of the business connection
    * @param showGiftButton
    *   Pass True, if a button for sending a gift to the user or by the business account must always be shown in the
    *   input field
    * @param acceptedGiftTypes
    *   Types of gifts accepted by the business account
    */
  def setBusinessAccountGiftSettings(
    businessConnectionId: String,
    showGiftButton: Boolean,
    acceptedGiftTypes: AcceptedGiftTypes
  ): Method[Boolean] = {
    val req = SetBusinessAccountGiftSettingsReq(businessConnectionId, showGiftButton, acceptedGiftTypes)
    MethodReq[Boolean]("setBusinessAccountGiftSettings", req.asJson)
  }

  /** Changes the first and last name of a managed business account. Requires the can_change_name business bot right.
    * Returns True on success.
    *
    * @param businessConnectionId
    *   Unique identifier of the business connection
    * @param firstName
    *   The new value of the first name for the business account; 1-64 characters
    * @param lastName
    *   The new value of the last name for the business account; 0-64 characters
    */
  def setBusinessAccountName(
    businessConnectionId: String,
    firstName: String,
    lastName: Option[String] = Option.empty
  ): Method[Boolean] = {
    val req = SetBusinessAccountNameReq(businessConnectionId, firstName, lastName)
    MethodReq[Boolean]("setBusinessAccountName", req.asJson)
  }

  /** Changes the profile photo of a managed business account. Requires the can_edit_profile_photo business bot right.
    * Returns True on success.
    *
    * @param businessConnectionId
    *   Unique identifier of the business connection
    * @param photo
    *   The new profile photo to set
    * @param isPublic
    *   Pass True to set the public photo, which will be visible even if the main photo is hidden by the business
    *   account's privacy settings. An account can have only one public photo.
    */
  def setBusinessAccountProfilePhoto(
    businessConnectionId: String,
    photo: InputProfilePhoto,
    isPublic: Option[Boolean] = Option.empty
  ): Method[Boolean] = {
    val req = SetBusinessAccountProfilePhotoReq(businessConnectionId, photo, isPublic)
    MethodReq[Boolean]("setBusinessAccountProfilePhoto", req.asJson)
  }

  /** Changes the username of a managed business account. Requires the can_change_username business bot right. Returns
    * True on success.
    *
    * @param businessConnectionId
    *   Unique identifier of the business connection
    * @param username
    *   The new value of the username for the business account; 0-32 characters
    */
  def setBusinessAccountUsername(
    businessConnectionId: String,
    username: Option[String] = Option.empty
  ): Method[Boolean] = {
    val req = SetBusinessAccountUsernameReq(businessConnectionId, username)
    MethodReq[Boolean]("setBusinessAccountUsername", req.asJson)
  }

  /** Use this method to set a custom title for an administrator in a supergroup promoted by the bot. Returns True on
    * success.
    *
    * @param chatId
    *   Unique identifier for the target chat or username of the target supergroup (in the format
    *   &#064;supergroupusername)
    * @param userId
    *   Unique identifier of the target user
    * @param customTitle
    *   New custom title for the administrator; 0-16 characters, emoji are not allowed
    */
  def setChatAdministratorCustomTitle(chatId: ChatId, userId: Long, customTitle: String): Method[Boolean] = {
    val req = SetChatAdministratorCustomTitleReq(chatId, userId, customTitle)
    MethodReq[Boolean]("setChatAdministratorCustomTitle", req.asJson)
  }

  /** Use this method to change the description of a group, a supergroup or a channel. The bot must be an administrator
    * in the chat for this to work and must have the appropriate administrator rights. Returns True on success.
    *
    * @param chatId
    *   Unique identifier for the target chat or username of the target channel (in the format &#064;channelusername)
    * @param description
    *   New chat description, 0-255 characters
    */
  def setChatDescription(chatId: ChatId, description: Option[String] = Option.empty): Method[Boolean] = {
    val req = SetChatDescriptionReq(chatId, description)
    MethodReq[Boolean]("setChatDescription", req.asJson)
  }

  /** Use this method to change the bot's menu button in a private chat, or the default menu button. Returns True on
    * success.
    *
    * @param chatId
    *   Unique identifier for the target private chat. If not specified, default bot's menu button will be changed
    * @param menuButton
    *   A JSON-serialized object for the bot's new menu button. Defaults to MenuButtonDefault
    */
  def setChatMenuButton(
    chatId: Option[Long] = Option.empty,
    menuButton: Option[MenuButton] = Option.empty
  ): Method[Boolean] = {
    val req = SetChatMenuButtonReq(chatId, menuButton)
    MethodReq[Boolean]("setChatMenuButton", req.asJson)
  }

  /** Use this method to set default chat permissions for all members. The bot must be an administrator in the group or
    * a supergroup for this to work and must have the can_restrict_members administrator rights. Returns True on
    * success.
    *
    * @param chatId
    *   Unique identifier for the target chat or username of the target supergroup (in the format
    *   &#064;supergroupusername)
    * @param permissions
    *   A JSON-serialized object for new default chat permissions
    * @param useIndependentChatPermissions
    *   Pass True if chat permissions are set independently. Otherwise, the can_send_other_messages and
    *   can_add_web_page_previews permissions will imply the can_send_messages, can_send_audios, can_send_documents,
    *   can_send_photos, can_send_videos, can_send_video_notes, and can_send_voice_notes permissions; the can_send_polls
    *   permission will imply the can_send_messages permission.
    */
  def setChatPermissions(
    chatId: ChatId,
    permissions: ChatPermissions,
    useIndependentChatPermissions: Option[Boolean] = Option.empty
  ): Method[Boolean] = {
    val req = SetChatPermissionsReq(chatId, permissions, useIndependentChatPermissions)
    MethodReq[Boolean]("setChatPermissions", req.asJson)
  }

  /** Use this method to set a new profile photo for the chat. Photos can't be changed for private chats. The bot must
    * be an administrator in the chat for this to work and must have the appropriate administrator rights. Returns True
    * on success.
    *
    * @param chatId
    *   Unique identifier for the target chat or username of the target channel (in the format &#064;channelusername)
    * @param photo
    *   New chat photo, uploaded using multipart/form-data
    */
  def setChatPhoto(chatId: ChatId, photo: IFile): Method[Boolean] = {
    val req = SetChatPhotoReq(chatId, photo)
    MethodReq[Boolean](
      "setChatPhoto",
      req.asJson,
      Map("photo" -> Option(photo)).collect { case (k, Some(v)) => k -> v }
    )
  }

  /** Use this method to set a new group sticker set for a supergroup. The bot must be an administrator in the chat for
    * this to work and must have the appropriate administrator rights. Use the field can_set_sticker_set optionally
    * returned in getChat requests to check if the bot can use this method. Returns True on success.
    *
    * @param chatId
    *   Unique identifier for the target chat or username of the target supergroup (in the format
    *   &#064;supergroupusername)
    * @param stickerSetName
    *   Name of the sticker set to be set as the group sticker set
    */
  def setChatStickerSet(chatId: ChatId, stickerSetName: String): Method[Boolean] = {
    val req = SetChatStickerSetReq(chatId, stickerSetName)
    MethodReq[Boolean]("setChatStickerSet", req.asJson)
  }

  /** Use this method to change the title of a chat. Titles can't be changed for private chats. The bot must be an
    * administrator in the chat for this to work and must have the appropriate administrator rights. Returns True on
    * success.
    *
    * @param chatId
    *   Unique identifier for the target chat or username of the target channel (in the format &#064;channelusername)
    * @param title
    *   New chat title, 1-128 characters
    */
  def setChatTitle(chatId: ChatId, title: String): Method[Boolean] = {
    val req = SetChatTitleReq(chatId, title)
    MethodReq[Boolean]("setChatTitle", req.asJson)
  }

  /** Use this method to set the thumbnail of a custom emoji sticker set. Returns True on success.
    *
    * @param name
    *   Sticker set name
    * @param customEmojiId
    *   Custom emoji identifier of a sticker from the sticker set; pass an empty string to drop the thumbnail and use
    *   the first sticker as the thumbnail.
    */
  def setCustomEmojiStickerSetThumbnail(name: String, customEmojiId: Option[String] = Option.empty): Method[Boolean] = {
    val req = SetCustomEmojiStickerSetThumbnailReq(name, customEmojiId)
    MethodReq[Boolean]("setCustomEmojiStickerSetThumbnail", req.asJson)
  }

  /** Use this method to set the score of the specified user in a game message. On success, if the message is not an
    * inline message, the Message is returned, otherwise True is returned. Returns an error, if the new score is not
    * greater than the user's current score in the chat and force is False.
    *
    * @param userId
    *   User identifier
    * @param score
    *   New score, must be non-negative
    * @param force
    *   Pass True if the high score is allowed to decrease. This can be useful when fixing mistakes or banning cheaters
    * @param disableEditMessage
    *   Pass True if the game message should not be automatically edited to include the current scoreboard
    * @param chatId
    *   Required if inline_message_id is not specified. Unique identifier for the target chat
    * @param messageId
    *   Required if inline_message_id is not specified. Identifier of the sent message
    * @param inlineMessageId
    *   Required if chat_id and message_id are not specified. Identifier of the inline message
    */
  def setGameScore(
    userId: Long,
    score: Int,
    force: Option[Boolean] = Option.empty,
    disableEditMessage: Option[Boolean] = Option.empty,
    chatId: Option[Long] = Option.empty,
    messageId: Option[Int] = Option.empty,
    inlineMessageId: Option[String] = Option.empty
  ): Method[Either[Boolean, Message]] = {
    val req = SetGameScoreReq(userId, score, force, disableEditMessage, chatId, messageId, inlineMessageId)
    MethodReq[Either[Boolean, Message]]("setGameScore", req.asJson)
  }

  /** Use this method to change the chosen reactions on a message. Service messages of some types can't be reacted to.
    * Automatically forwarded messages from a channel to its discussion group have the same available reactions as
    * messages in the channel. Bots can't use paid reactions. Returns True on success.
    *
    * @param chatId
    *   Unique identifier for the target chat or username of the target channel (in the format &#064;channelusername)
    * @param messageId
    *   Identifier of the target message. If the message belongs to a media group, the reaction is set to the first
    *   non-deleted message in the group instead.
    * @param reaction
    *   A JSON-serialized list of reaction types to set on the message. Currently, as non-premium users, bots can set up
    *   to one reaction per message. A custom emoji reaction can be used if it is either already present on the message
    *   or explicitly allowed by chat administrators. Paid reactions can't be used by bots.
    * @param isBig
    *   Pass True to set the reaction with a big animation
    */
  def setMessageReaction(
    chatId: ChatId,
    messageId: Int,
    reaction: List[ReactionType] = List.empty,
    isBig: Option[Boolean] = Option.empty
  ): Method[Boolean] = {
    val req = SetMessageReactionReq(chatId, messageId, reaction, isBig)
    MethodReq[Boolean]("setMessageReaction", req.asJson)
  }

  /** Use this method to change the list of the bot's commands. See this manual for more details about bot commands.
    * Returns True on success.
    *
    * @param commands
    *   A JSON-serialized list of bot commands to be set as the list of the bot's commands. At most 100 commands can be
    *   specified.
    * @param scope
    *   A JSON-serialized object, describing scope of users for which the commands are relevant. Defaults to
    *   BotCommandScopeDefault.
    * @param languageCode
    *   A two-letter ISO 639-1 language code. If empty, commands will be applied to all users from the given scope, for
    *   whose language there are no dedicated commands
    */
  def setMyCommands(
    commands: List[BotCommand] = List.empty,
    scope: Option[BotCommandScope] = Option.empty,
    languageCode: Option[String] = Option.empty
  ): Method[Boolean] = {
    val req = SetMyCommandsReq(commands, scope, languageCode)
    MethodReq[Boolean]("setMyCommands", req.asJson)
  }

  /** Use this method to change the default administrator rights requested by the bot when it's added as an
    * administrator to groups or channels. These rights will be suggested to users, but they are free to modify the list
    * before adding the bot. Returns True on success.
    *
    * @param rights
    *   A JSON-serialized object describing new default administrator rights. If not specified, the default
    *   administrator rights will be cleared.
    * @param forChannels
    *   Pass True to change the default administrator rights of the bot in channels. Otherwise, the default
    *   administrator rights of the bot for groups and supergroups will be changed.
    */
  def setMyDefaultAdministratorRights(
    rights: Option[ChatAdministratorRights] = Option.empty,
    forChannels: Option[Boolean] = Option.empty
  ): Method[Boolean] = {
    val req = SetMyDefaultAdministratorRightsReq(rights, forChannels)
    MethodReq[Boolean]("setMyDefaultAdministratorRights", req.asJson)
  }

  /** Use this method to change the bot's description, which is shown in the chat with the bot if the chat is empty.
    * Returns True on success.
    *
    * @param description
    *   New bot description; 0-512 characters. Pass an empty string to remove the dedicated description for the given
    *   language.
    * @param languageCode
    *   A two-letter ISO 639-1 language code. If empty, the description will be applied to all users for whose language
    *   there is no dedicated description.
    */
  def setMyDescription(
    description: Option[String] = Option.empty,
    languageCode: Option[String] = Option.empty
  ): Method[Boolean] = {
    val req = SetMyDescriptionReq(description, languageCode)
    MethodReq[Boolean]("setMyDescription", req.asJson)
  }

  /** Use this method to change the bot's name. Returns True on success.
    *
    * @param name
    *   New bot name; 0-64 characters. Pass an empty string to remove the dedicated name for the given language.
    * @param languageCode
    *   A two-letter ISO 639-1 language code. If empty, the name will be shown to all users for whose language there is
    *   no dedicated name.
    */
  def setMyName(name: Option[String] = Option.empty, languageCode: Option[String] = Option.empty): Method[Boolean] = {
    val req = SetMyNameReq(name, languageCode)
    MethodReq[Boolean]("setMyName", req.asJson)
  }

  /** Use this method to change the bot's short description, which is shown on the bot's profile page and is sent
    * together with the link when users share the bot. Returns True on success.
    *
    * @param shortDescription
    *   New short description for the bot; 0-120 characters. Pass an empty string to remove the dedicated short
    *   description for the given language.
    * @param languageCode
    *   A two-letter ISO 639-1 language code. If empty, the short description will be applied to all users for whose
    *   language there is no dedicated short description.
    */
  def setMyShortDescription(
    shortDescription: Option[String] = Option.empty,
    languageCode: Option[String] = Option.empty
  ): Method[Boolean] = {
    val req = SetMyShortDescriptionReq(shortDescription, languageCode)
    MethodReq[Boolean]("setMyShortDescription", req.asJson)
  }

  /** Informs a user that some of the Telegram Passport elements they provided contains errors. The user will not be
    * able to re-submit their Passport to you until the errors are fixed (the contents of the field for which you
    * returned the error must change). Returns True on success. Use this if the data submitted by the user doesn't
    * satisfy the standards your service requires for any reason. For example, if a birthday date seems invalid, a
    * submitted document is blurry, a scan shows evidence of tampering, etc. Supply some details in the error message to
    * make sure the user knows how to correct the issues.
    *
    * @param userId
    *   User identifier
    * @param errors
    *   A JSON-serialized array describing the errors
    */
  def setPassportDataErrors(userId: Long, errors: List[PassportElementError] = List.empty): Method[Boolean] = {
    val req = SetPassportDataErrorsReq(userId, errors)
    MethodReq[Boolean]("setPassportDataErrors", req.asJson)
  }

  /** Use this method to change the list of emoji assigned to a regular or custom emoji sticker. The sticker must belong
    * to a sticker set created by the bot. Returns True on success.
    *
    * @param sticker
    *   File identifier of the sticker
    * @param emojiList
    *   A JSON-serialized list of 1-20 emoji associated with the sticker
    */
  def setStickerEmojiList(sticker: String, emojiList: List[String] = List.empty): Method[Boolean] = {
    val req = SetStickerEmojiListReq(sticker, emojiList)
    MethodReq[Boolean]("setStickerEmojiList", req.asJson)
  }

  /** Use this method to change search keywords assigned to a regular or custom emoji sticker. The sticker must belong
    * to a sticker set created by the bot. Returns True on success.
    *
    * @param sticker
    *   File identifier of the sticker
    * @param keywords
    *   A JSON-serialized list of 0-20 search keywords for the sticker with total length of up to 64 characters
    */
  def setStickerKeywords(sticker: String, keywords: List[String] = List.empty): Method[Boolean] = {
    val req = SetStickerKeywordsReq(sticker, keywords)
    MethodReq[Boolean]("setStickerKeywords", req.asJson)
  }

  /** Use this method to change the mask position of a mask sticker. The sticker must belong to a sticker set that was
    * created by the bot. Returns True on success.
    *
    * @param sticker
    *   File identifier of the sticker
    * @param maskPosition
    *   A JSON-serialized object with the position where the mask should be placed on faces. Omit the parameter to
    *   remove the mask position.
    */
  def setStickerMaskPosition(sticker: String, maskPosition: Option[MaskPosition] = Option.empty): Method[Boolean] = {
    val req = SetStickerMaskPositionReq(sticker, maskPosition)
    MethodReq[Boolean]("setStickerMaskPosition", req.asJson)
  }

  /** Use this method to move a sticker in a set created by the bot to a specific position. Returns True on success.
    *
    * @param sticker
    *   File identifier of the sticker
    * @param position
    *   New sticker position in the set, zero-based
    */
  def setStickerPositionInSet(sticker: String, position: Int): Method[Boolean] = {
    val req = SetStickerPositionInSetReq(sticker, position)
    MethodReq[Boolean]("setStickerPositionInSet", req.asJson)
  }

  /** Use this method to set the thumbnail of a regular or mask sticker set. The format of the thumbnail file must match
    * the format of the stickers in the set. Returns True on success.
    *
    * @param name
    *   Sticker set name
    * @param userId
    *   User identifier of the sticker set owner
    * @param format
    *   Format of the thumbnail, must be one of “static” for a .WEBP or .PNG image, “animated” for a .TGS animation, or
    *   “video” for a .WEBM video
    * @param thumbnail
    *   A .WEBP or .PNG image with the thumbnail, must be up to 128 kilobytes in size and have a width and height of
    *   exactly 100px, or a .TGS animation with a thumbnail up to 32 kilobytes in size (see
    *   https://core.telegram.org/stickers#animation-requirements for animated sticker technical requirements), or a
    *   .WEBM video with the thumbnail up to 32 kilobytes in size; see
    *   https://core.telegram.org/stickers#video-requirements for video sticker technical requirements. Pass a file_id
    *   as a String to send a file that already exists on the Telegram servers, pass an HTTP URL as a String for
    *   Telegram to get a file from the Internet, or upload a new one using multipart/form-data. Animated and video
    *   sticker set thumbnails can't be uploaded via HTTP URL. If omitted, then the thumbnail is dropped and the first
    *   sticker is used as the thumbnail.
    */
  def setStickerSetThumbnail(
    name: String,
    userId: Long,
    format: String,
    thumbnail: Option[IFile] = Option.empty
  ): Method[Boolean] = {
    val req = SetStickerSetThumbnailReq(name, userId, format, thumbnail)
    MethodReq[Boolean](
      "setStickerSetThumbnail",
      req.asJson,
      Map("thumbnail" -> thumbnail).collect { case (k, Some(v)) => k -> v }
    )
  }

  /** Use this method to set the title of a created sticker set. Returns True on success.
    *
    * @param name
    *   Sticker set name
    * @param title
    *   Sticker set title, 1-64 characters
    */
  def setStickerSetTitle(name: String, title: String): Method[Boolean] = {
    val req = SetStickerSetTitleReq(name, title)
    MethodReq[Boolean]("setStickerSetTitle", req.asJson)
  }

  /** Changes the emoji status for a given user that previously allowed the bot to manage their emoji status via the
    * Mini App method requestEmojiStatusAccess. Returns True on success.
    *
    * @param userId
    *   Unique identifier of the target user
    * @param emojiStatusCustomEmojiId
    *   Custom emoji identifier of the emoji status to set. Pass an empty string to remove the status.
    * @param emojiStatusExpirationDate
    *   Expiration date of the emoji status, if any
    */
  def setUserEmojiStatus(
    userId: Long,
    emojiStatusCustomEmojiId: Option[String] = Option.empty,
    emojiStatusExpirationDate: Option[Int] = Option.empty
  ): Method[Boolean] = {
    val req = SetUserEmojiStatusReq(userId, emojiStatusCustomEmojiId, emojiStatusExpirationDate)
    MethodReq[Boolean]("setUserEmojiStatus", req.asJson)
  }

  /** Use this method to specify a URL and receive incoming updates via an outgoing webhook. Whenever there is an update
    * for the bot, we will send an HTTPS POST request to the specified URL, containing a JSON-serialized Update. In case
    * of an unsuccessful request (a request with response HTTP status code different from 2XY), we will repeat the
    * request and give up after a reasonable amount of attempts. Returns True on success. If you'd like to make sure
    * that the webhook was set by you, you can specify secret data in the parameter secret_token. If specified, the
    * request will contain a header “X-Telegram-Bot-Api-Secret-Token” with the secret token as content.
    *
    * @param url
    *   HTTPS URL to send updates to. Use an empty string to remove webhook integration
    * @param certificate
    *   Upload your public key certificate so that the root certificate in use can be checked. See our self-signed guide
    *   for details.
    * @param ipAddress
    *   The fixed IP address which will be used to send webhook requests instead of the IP address resolved through DNS
    * @param maxConnections
    *   The maximum allowed number of simultaneous HTTPS connections to the webhook for update delivery, 1-100. Defaults
    *   to 40. Use lower values to limit the load on your bot's server, and higher values to increase your bot's
    *   throughput.
    * @param allowedUpdates
    *   A JSON-serialized list of the update types you want your bot to receive. For example, specify ["message",
    *   "edited_channel_post", "callback_query"] to only receive updates of these types. See Update for a complete list
    *   of available update types. Specify an empty list to receive all update types except chat_member,
    *   message_reaction, and message_reaction_count (default). If not specified, the previous setting will be used.
    *   Please note that this parameter doesn't affect updates created before the call to the setWebhook, so unwanted
    *   updates may be received for a short period of time.
    * @param dropPendingUpdates
    *   Pass True to drop all pending updates
    * @param secretToken
    *   A secret token to be sent in a header “X-Telegram-Bot-Api-Secret-Token” in every webhook request, 1-256
    *   characters. Only characters A-Z, a-z, 0-9, _ and - are allowed. The header is useful to ensure that the request
    *   comes from a webhook set by you.
    */
  def setWebhook(
    url: String,
    certificate: Option[IFile] = Option.empty,
    ipAddress: Option[String] = Option.empty,
    maxConnections: Option[Int] = Option.empty,
    allowedUpdates: List[String] = List.empty,
    dropPendingUpdates: Option[Boolean] = Option.empty,
    secretToken: Option[String] = Option.empty
  ): Method[Boolean] = {
    val req =
      SetWebhookReq(url, certificate, ipAddress, maxConnections, allowedUpdates, dropPendingUpdates, secretToken)
    MethodReq[Boolean](
      "setWebhook",
      req.asJson,
      Map("certificate" -> certificate).collect { case (k, Some(v)) => k -> v }
    )
  }

  /** Use this method to stop updating a live location message before live_period expires. On success, if the message is
    * not an inline message, the edited Message is returned, otherwise True is returned.
    *
    * @param businessConnectionId
    *   Unique identifier of the business connection on behalf of which the message to be edited was sent
    * @param chatId
    *   Required if inline_message_id is not specified. Unique identifier for the target chat or username of the target
    *   channel (in the format &#064;channelusername)
    * @param messageId
    *   Required if inline_message_id is not specified. Identifier of the message with live location to stop
    * @param inlineMessageId
    *   Required if chat_id and message_id are not specified. Identifier of the inline message
    * @param replyMarkup
    *   A JSON-serialized object for a new inline keyboard.
    */
  def stopMessageLiveLocation(
    businessConnectionId: Option[String] = Option.empty,
    chatId: Option[ChatId] = Option.empty,
    messageId: Option[Int] = Option.empty,
    inlineMessageId: Option[String] = Option.empty,
    replyMarkup: Option[InlineKeyboardMarkup] = Option.empty
  ): Method[Either[Boolean, Message]] = {
    val req = StopMessageLiveLocationReq(businessConnectionId, chatId, messageId, inlineMessageId, replyMarkup)
    MethodReq[Either[Boolean, Message]]("stopMessageLiveLocation", req.asJson)
  }

  /** Use this method to stop a poll which was sent by the bot. On success, the stopped Poll is returned.
    *
    * @param chatId
    *   Unique identifier for the target chat or username of the target channel (in the format &#064;channelusername)
    * @param messageId
    *   Identifier of the original message with the poll
    * @param businessConnectionId
    *   Unique identifier of the business connection on behalf of which the message to be edited was sent
    * @param replyMarkup
    *   A JSON-serialized object for a new message inline keyboard.
    */
  def stopPoll(
    chatId: ChatId,
    messageId: Int,
    businessConnectionId: Option[String] = Option.empty,
    replyMarkup: Option[InlineKeyboardMarkup] = Option.empty
  ): Method[Poll] = {
    val req = StopPollReq(chatId, messageId, businessConnectionId, replyMarkup)
    MethodReq[Poll]("stopPoll", req.asJson)
  }

  /** Transfers Telegram Stars from the business account balance to the bot's balance. Requires the can_transfer_stars
    * business bot right. Returns True on success.
    *
    * @param businessConnectionId
    *   Unique identifier of the business connection
    * @param starCount
    *   Number of Telegram Stars to transfer; 1-10000
    */
  def transferBusinessAccountStars(businessConnectionId: String, starCount: Int): Method[Boolean] = {
    val req = TransferBusinessAccountStarsReq(businessConnectionId, starCount)
    MethodReq[Boolean]("transferBusinessAccountStars", req.asJson)
  }

  /** Transfers an owned unique gift to another user. Requires the can_transfer_and_upgrade_gifts business bot right.
    * Requires can_transfer_stars business bot right if the transfer is paid. Returns True on success.
    *
    * @param businessConnectionId
    *   Unique identifier of the business connection
    * @param ownedGiftId
    *   Unique identifier of the regular gift that should be transferred
    * @param newOwnerChatId
    *   Unique identifier of the chat which will own the gift. The chat must be active in the last 24 hours.
    * @param starCount
    *   The amount of Telegram Stars that will be paid for the transfer from the business account balance. If positive,
    *   then the can_transfer_stars business bot right is required.
    */
  def transferGift(
    businessConnectionId: String,
    ownedGiftId: String,
    newOwnerChatId: Long,
    starCount: Option[Int] = Option.empty
  ): Method[Boolean] = {
    val req = TransferGiftReq(businessConnectionId, ownedGiftId, newOwnerChatId, starCount)
    MethodReq[Boolean]("transferGift", req.asJson)
  }

  /** Use this method to unban a previously banned user in a supergroup or channel. The user will not return to the
    * group or channel automatically, but will be able to join via link, etc. The bot must be an administrator for this
    * to work. By default, this method guarantees that after the call the user is not a member of the chat, but will be
    * able to join it. So if the user is a member of the chat they will also be removed from the chat. If you don't want
    * this, use the parameter only_if_banned. Returns True on success.
    *
    * @param chatId
    *   Unique identifier for the target group or username of the target supergroup or channel (in the format
    *   &#064;channelusername)
    * @param userId
    *   Unique identifier of the target user
    * @param onlyIfBanned
    *   Do nothing if the user is not banned
    */
  def unbanChatMember(chatId: ChatId, userId: Long, onlyIfBanned: Option[Boolean] = Option.empty): Method[Boolean] = {
    val req = UnbanChatMemberReq(chatId, userId, onlyIfBanned)
    MethodReq[Boolean]("unbanChatMember", req.asJson)
  }

  /** Use this method to unban a previously banned channel chat in a supergroup or channel. The bot must be an
    * administrator for this to work and must have the appropriate administrator rights. Returns True on success.
    *
    * @param chatId
    *   Unique identifier for the target chat or username of the target channel (in the format &#064;channelusername)
    * @param senderChatId
    *   Unique identifier of the target sender chat
    */
  def unbanChatSenderChat(chatId: ChatId, senderChatId: Long): Method[Boolean] = {
    val req = UnbanChatSenderChatReq(chatId, senderChatId)
    MethodReq[Boolean]("unbanChatSenderChat", req.asJson)
  }

  /** Use this method to unhide the 'General' topic in a forum supergroup chat. The bot must be an administrator in the
    * chat for this to work and must have the can_manage_topics administrator rights. Returns True on success.
    *
    * @param chatId
    *   Unique identifier for the target chat or username of the target supergroup (in the format
    *   &#064;supergroupusername)
    */
  def unhideGeneralForumTopic(chatId: ChatId): Method[Boolean] = {
    val req = UnhideGeneralForumTopicReq(chatId)
    MethodReq[Boolean]("unhideGeneralForumTopic", req.asJson)
  }

  /** Use this method to clear the list of pinned messages in a chat. If the chat is not a private chat, the bot must be
    * an administrator in the chat for this to work and must have the 'can_pin_messages' administrator right in a
    * supergroup or 'can_edit_messages' administrator right in a channel. Returns True on success.
    *
    * @param chatId
    *   Unique identifier for the target chat or username of the target channel (in the format &#064;channelusername)
    */
  def unpinAllChatMessages(chatId: ChatId): Method[Boolean] = {
    val req = UnpinAllChatMessagesReq(chatId)
    MethodReq[Boolean]("unpinAllChatMessages", req.asJson)
  }

  /** Use this method to clear the list of pinned messages in a forum topic. The bot must be an administrator in the
    * chat for this to work and must have the can_pin_messages administrator right in the supergroup. Returns True on
    * success.
    *
    * @param chatId
    *   Unique identifier for the target chat or username of the target supergroup (in the format
    *   &#064;supergroupusername)
    * @param messageThreadId
    *   Unique identifier for the target message thread of the forum topic
    */
  def unpinAllForumTopicMessages(chatId: ChatId, messageThreadId: Int): Method[Boolean] = {
    val req = UnpinAllForumTopicMessagesReq(chatId, messageThreadId)
    MethodReq[Boolean]("unpinAllForumTopicMessages", req.asJson)
  }

  /** Use this method to clear the list of pinned messages in a General forum topic. The bot must be an administrator in
    * the chat for this to work and must have the can_pin_messages administrator right in the supergroup. Returns True
    * on success.
    *
    * @param chatId
    *   Unique identifier for the target chat or username of the target supergroup (in the format
    *   &#064;supergroupusername)
    */
  def unpinAllGeneralForumTopicMessages(chatId: ChatId): Method[Boolean] = {
    val req = UnpinAllGeneralForumTopicMessagesReq(chatId)
    MethodReq[Boolean]("unpinAllGeneralForumTopicMessages", req.asJson)
  }

  /** Use this method to remove a message from the list of pinned messages in a chat. If the chat is not a private chat,
    * the bot must be an administrator in the chat for this to work and must have the 'can_pin_messages' administrator
    * right in a supergroup or 'can_edit_messages' administrator right in a channel. Returns True on success.
    *
    * @param chatId
    *   Unique identifier for the target chat or username of the target channel (in the format &#064;channelusername)
    * @param businessConnectionId
    *   Unique identifier of the business connection on behalf of which the message will be unpinned
    * @param messageId
    *   Identifier of the message to unpin. Required if business_connection_id is specified. If not specified, the most
    *   recent pinned message (by sending date) will be unpinned.
    */
  def unpinChatMessage(
    chatId: ChatId,
    businessConnectionId: Option[String] = Option.empty,
    messageId: Option[Int] = Option.empty
  ): Method[Boolean] = {
    val req = UnpinChatMessageReq(chatId, businessConnectionId, messageId)
    MethodReq[Boolean]("unpinChatMessage", req.asJson)
  }

  /** Upgrades a given regular gift to a unique gift. Requires the can_transfer_and_upgrade_gifts business bot right.
    * Additionally requires the can_transfer_stars business bot right if the upgrade is paid. Returns True on success.
    *
    * @param businessConnectionId
    *   Unique identifier of the business connection
    * @param ownedGiftId
    *   Unique identifier of the regular gift that should be upgraded to a unique one
    * @param keepOriginalDetails
    *   Pass True to keep the original gift text, sender and receiver in the upgraded gift
    * @param starCount
    *   The amount of Telegram Stars that will be paid for the upgrade from the business account balance. If
    *   gift.prepaid_upgrade_star_count > 0, then pass 0, otherwise, the can_transfer_stars business bot right is
    *   required and gift.upgrade_star_count must be passed.
    */
  def upgradeGift(
    businessConnectionId: String,
    ownedGiftId: String,
    keepOriginalDetails: Option[Boolean] = Option.empty,
    starCount: Option[Int] = Option.empty
  ): Method[Boolean] = {
    val req = UpgradeGiftReq(businessConnectionId, ownedGiftId, keepOriginalDetails, starCount)
    MethodReq[Boolean]("upgradeGift", req.asJson)
  }

  /** Use this method to upload a file with a sticker for later use in the createNewStickerSet, addStickerToSet, or
    * replaceStickerInSet methods (the file can be used multiple times). Returns the uploaded File on success.
    *
    * @param userId
    *   User identifier of sticker file owner
    * @param sticker
    *   A file with the sticker in .WEBP, .PNG, .TGS, or .WEBM format. See https://core.telegram.org/stickers for
    *   technical requirements.
    * @param stickerFormat
    *   Format of the sticker, must be one of “static”, “animated”, “video”
    */
  def uploadStickerFile(userId: Long, sticker: IFile, stickerFormat: String): Method[File] = {
    val req = UploadStickerFileReq(userId, sticker, stickerFormat)
    MethodReq[File](
      "uploadStickerFile",
      req.asJson,
      Map("sticker" -> Option(sticker)).collect { case (k, Some(v)) => k -> v }
    )
  }

  /** Verifies a chat on behalf of the organization which is represented by the bot. Returns True on success.
    *
    * @param chatId
    *   Unique identifier for the target chat or username of the target channel (in the format &#064;channelusername)
    * @param customDescription
    *   Custom description for the verification; 0-70 characters. Must be empty if the organization isn't allowed to
    *   provide a custom verification description.
    */
  def verifyChat(chatId: ChatId, customDescription: Option[String] = Option.empty): Method[Boolean] = {
    val req = VerifyChatReq(chatId, customDescription)
    MethodReq[Boolean]("verifyChat", req.asJson)
  }

  /** Verifies a user on behalf of the organization which is represented by the bot. Returns True on success.
    *
    * @param userId
    *   Unique identifier of the target user
    * @param customDescription
    *   Custom description for the verification; 0-70 characters. Must be empty if the organization isn't allowed to
    *   provide a custom verification description.
    */
  def verifyUser(userId: Long, customDescription: Option[String] = Option.empty): Method[Boolean] = {
    val req = VerifyUserReq(userId, customDescription)
    MethodReq[Boolean]("verifyUser", req.asJson)
  }

}
