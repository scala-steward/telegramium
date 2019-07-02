package telegramium.bots.client

import telegramium.bots.ShippingOption

final case class AnswerShippingQueryReq(
                                        /** Unique identifier for the query to be answered*/
                                        shippingQueryId: String,
                                        /** Specify True if delivery to the specified address is
                                          * possible and False if there are any problems (for example,
                                          * if delivery to the specified address is not possible)*/
                                        ok: Boolean,
                                        /** Required if ok is True. A JSON-serialized array of
                                          * available shipping options.*/
                                        shippingOptions: List[ShippingOption] = List.empty,
                                        /** Required if ok is False. Error message in human readable
                                          * form that explains why it is impossible to complete the
                                          * order (e.g. "Sorry, delivery to your desired address is
                                          * unavailable'). Telegram will display this message to the
                                          * user.*/
                                        errorMessage: Option[String] = Option.empty)