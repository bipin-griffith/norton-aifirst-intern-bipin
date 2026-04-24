package com.norton.scamdetector.data.model

data class ExampleMessage(
    val title: String,
    val messageText: String
) {
    companion object {
        fun getExamples(): List<ExampleMessage> = listOf(
            ExampleMessage(
                title = "Fake Bank SMS",
                messageText = "URGENT: Your bank account has been suspended. Verify your identity immediately at http://secure-bank-verify.xyz or lose access"
            ),
            ExampleMessage(
                title = "Prize Scam",
                messageText = "Congratulations! You've been selected to receive a \$1000 Amazon gift card. Claim it now at bit.ly/free-gift-claim before it expires in 24 hours. Reply STOP to opt out"
            )
        )
    }
}
