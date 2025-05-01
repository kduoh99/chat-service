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
  import SockJS from 'sockjs-client';
  import Stomp from 'webstomp-client';
  // import axios from 'axios';

  export default {
    data() {
      return {
        messages: [],
        newMessage: '',
        stompClient: null,
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
        const sockJs = new SockJS(`${process.env.VUE_APP_API_BASE_URL}/connect`);
        this.stompClient = Stomp.over(sockJs);

        this.stompClient.connect({}, () => {
          this.stompClient.subscribe(`/topic/1`, (message) => {
            this.messages.push(message.body);
            this.scrollToBottom();
          });
        });
      },
      send() {
        if (this.newMessage.trim() === '') return;
        this.stompClient.send(`/publish/1`, this.newMessage);
        this.newMessage = '';
      },
      scrollToBottom() {
        this.$nextTick(() => {
          const chatBox = this.$el.querySelector('.chat-box');
          chatBox.scrollTop = chatBox.scrollHeight;
        });
      },
      disconnect() {
        // if (this.ws) {
        //   this.ws.close();
        //   console.log('Disconnected.');
        //   this.ws = null;
        // }
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
