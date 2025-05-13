<template>
  <v-container class="mt-5">
    <v-row justify="center" align="start">
      <v-col cols="12" sm="10" md="8" lg="6">
        <v-card>
          <v-card-title class="text-h5 text-center">채팅</v-card-title>
          <v-card-text>
            <div class="chat-box">
              <div
                v-for="(msg, index) in messages"
                :key="index"
                :class="['chat-message', msg.sender === sender ? 'sent' : 'received']"
              >
                <strong>{{ msg.sender }}:</strong> {{ msg.message }}
              </div>
            </div>
            <v-text-field v-model="newMessage" label="메시지 입력" @keyup.enter="send" outlined class="mb-3" />
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
  import axios from 'axios';

  export default {
    data() {
      return {
        messages: [],
        newMessage: '',
        stompClient: null,
        accessToken: '',
        sender: null,
        roomId: null,
      };
    },
    async created() {
      this.sender = localStorage.getItem('email');
      this.roomId = this.$route.params.roomId;
      const response = await axios.get(`${process.env.VUE_APP_API_BASE_URL}/api/chat/history/${this.roomId}`);
      this.messages = response.data;
      this.connect();
    },
    beforeRouteLeave(to, from, next) {
      this.disconnect();
      next();
    },
    beforeUnmount() {
      this.disconnect();
    },
    methods: {
      connect() {
        if (this.stompClient && this.stompClient.connected) return;
        const sockJs = new SockJS(`${process.env.VUE_APP_API_BASE_URL}/connect`);
        this.stompClient = Stomp.over(sockJs);
        this.accessToken = localStorage.getItem('accessToken');
        this.stompClient.connect(
          {
            Authorization: `Bearer ${this.accessToken}`,
          },
          () => {
            this.stompClient.subscribe(
              `/topic/${this.roomId}`,
              (message) => {
                const parseMessage = JSON.parse(message.body);
                this.messages.push(parseMessage);
                this.scrollToBottom();
              },
              {
                Authorization: `Bearer ${this.accessToken}`,
              },
            );
          },
        );
      },
      send() {
        if (this.newMessage.trim() === '') return;
        const message = {
          sender: this.sender,
          message: this.newMessage,
        };
        this.stompClient.send(`/publish/${this.roomId}`, JSON.stringify(message));
        this.newMessage = '';
      },
      scrollToBottom() {
        this.$nextTick(() => {
          const chatBox = this.$el.querySelector('.chat-box');
          chatBox.scrollTop = chatBox.scrollHeight;
        });
      },
      async disconnect() {
        await axios.post(`${process.env.VUE_APP_API_BASE_URL}/api/chat/room/${this.roomId}/read`);
        if (this.stompClient && this.stompClient.connected) {
          this.stompClient.unsubscribe(`/topic/${this.roomId}`);
          this.stompClient.disconnect();
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
    padding: 10px;
    margin-bottom: 16px;
    background-color: #f9f9f9;
  }

  .chat-message {
    margin-bottom: 10px;
    word-break: break-word;
  }

  .sent {
    text-align: right;
    color: #1e88e5;
  }

  .received {
    text-align: left;
    color: #333;
  }
</style>
