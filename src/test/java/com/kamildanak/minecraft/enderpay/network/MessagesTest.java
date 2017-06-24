package com.kamildanak.minecraft.enderpay.network;

class MessagesTest {
/*
    @Test
    void testMessagesEncoding() throws InstantiationException, IllegalAccessException {
        testMessageEncodeDecode(new MessageBalance(100, 19066393223815L));
        testMessageEncodeDecode(new MessageIssueBanknote(100));
        ISettings settings = new DummySettings("MyAwesomeCurrency AAAAAAAAA",
                "Test", Long.MAX_VALUE, false, Integer.MIN_VALUE, true,
                Integer.MAX_VALUE, 200, true, true,
                100,100,11,11,11, false);
        testMessageEncodeDecode(new MessageSettings(settings));
        settings = new DummySettings("ĄśćóżźćŻŹóóÓÓŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻŻ",
                "TestSCYGDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDD",
                Long.MAX_VALUE, false, Integer.MIN_VALUE, true,
                Integer.MAX_VALUE, 200, true, true,
                100,100,11,11,11, false);
        testMessageEncodeDecode(new MessageSettings(settings));
    }

    private void testMessageEncodeDecode(AbstractMessage message) throws IllegalAccessException, InstantiationException {
        ByteBuf buffer = Unpooled.buffer();
        message.toBytes(buffer);
        AbstractMessage returnMessage = message.getClass().newInstance();
        returnMessage.fromBytes(buffer);

        assertReflectionEquals(returnMessage, message);
    }
*/
}