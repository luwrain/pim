
wizard 'Подключение электронной почты', 'greeting', {


frame 'greeting', {
text getStrings().wizardIntro()
input 'mail-addr', getStrings().wizardMailAddr()
button getStrings().wizardContinue(), { values ->
error values.getText(0)
}
button getStrings().wizardSkip(), { skip() }

}
}


