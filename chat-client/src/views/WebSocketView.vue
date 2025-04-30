<template>
  <v-container>
    <v-row justify="center">
      <v-col cols="12" md="8">
        <v-card>
          <v-card-title class="text-h5 text-center">채팅</v-card-title>
          <v-card-text>
            <div class="chat-box">
              <div v-for="(msg, index) in messages" :key="index">{{ msg }}</div>
            </div>
            <v-text-field v-model="newMessage" label="메시지 입력" @keyup.enter="send" />
            <v-btn color="primary" block @click="send">전송</v-btn>
          </v-card-text>
        </v-card>
      </v-col>
    </v-row>
  </v-container>
</template>

<script>
  export default {
    data() {
      return {
        ws: null,
        messages: [],
        newMessage: '',
      };
    },
    created() {
      this.connect();
    },
    beforeUnmount() {
      this.disconnect();
    },
    methods: {
      connect() {
        this.ws = new WebSocket('ws://localhost:8080/connect');

        this.ws.onopen = () => {
          console.log('Successfully Connected.');
        };

        this.ws.onmessage = (message) => {
          this.messages.push(message.data);
          this.scrollToBottom();
        };

        this.ws.onclose = () => {
          console.log('Disconnected.');
        };
      },
      send() {
        if (this.newMessage.trim() === '') return;
        this.ws.send(this.newMessage);
        this.newMessage = '';
      },
      scrollToBottom() {
        this.$nextTick(() => {
          const chatBox = this.$el.querySelector('.chat-box');
          chatBox.scrollTop = chatBox.scrollHeight;
        });
      },
      disconnect() {
        if (this.ws) {
          this.ws.close();
          console.log('Disconnected.');
          this.ws = null;
        }
      },
    },
  };
</script>

<style>
  .chat-box {
    height: 300px;
    overflow-y: auto;
    border: 1px solid #ddd;
    margin-bottom: 10px;
  }
</style>
