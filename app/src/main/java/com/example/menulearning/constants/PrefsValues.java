package com.example.menulearning.constants;

public enum PrefsValues implements EnumDecoder {
    PREFS_NAME("menu_learning"),
    INTRO_TEXT_KEY("intro_text"),
    RULES_TEXT_KEY("rules_text"),
    INTRO_TEXT_VALUE("\tПриложение Menu Learning позволяет проверить знания меню среди " +
       "официантов вашего заведения, с целью их допуска к работе.\n\n"),
    RULES_TEXT_VALUE("\tНажмите на кнопку \"Начать\", чтобы пройти тестирование. " +
       "Вам будет предложено ответить на 8 вопросов. В каждом вопросе 3 варианта ответов, 1 из " +
       "которых верный. После завершения тестирования Вы можете посмотреть ваши результаты, " +
       "нажав на кнопку \"Результаты\"."),
    TEST_FLAG_KEY("test_flag"),
    DATA_INSERTED_FLAG_KEY("data_inserted"),
    USER_NAME("user_name");


    private String stringValue;

    PrefsValues(String stringValue) {
        this.stringValue = stringValue;
    }

    @Override
    public String getStringValue() {
        return stringValue;
    }
}
