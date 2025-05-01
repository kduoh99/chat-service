<template>
  <v-container>
    <v-row justify="center">
      <v-col cols="12" md="8">
        <v-card>
          <v-card-title class="text-h5 text-center">채팅</v-card-title>
          <v-card-text>
            <div class="chat-box">
              <div
                v-for="(msg, index) in messages"
                :key="index"
                :class="['chat-message', msg.sender === this.sender ? 'sent' : 'received']"
              >
                <strong>{{ msg.sender }}: </strong>{{ msg.message }}
              </div>
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
        accessToken: '',
        sender: null,
      };
    },
    // 컴포넌트가 생성될 때 실행됨
    created() {
      this.sender = localStorage.getItem('email');
      this.connect();
    },
    // 라우트 이동 직전 실행됨
    beforeRouteLeave(to, from, next) {
      this.disconnect();
      next();
    },
    // 컴포넌트가 제거되기 직전 실행됨
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
            this.stompClient.subscribe(`/topic/1`, (message) => {
              const parseMessage = JSON.parse(message.body);
              this.messages.push(parseMessage);
              this.scrollToBottom();
            });
          },
        );
      },
      send() {
        if (this.newMessage.trim() === '') return;
        const message = {
          sender: this.sender,
          message: this.newMessage,
        };
        this.stompClient.send(`/publish/1`, JSON.stringify(message));
        this.newMessage = '';
      },
      scrollToBottom() {
        this.$nextTick(() => {
          const chatBox = this.$el.querySelector('.chat-box');
          chatBox.scrollTop = chatBox.scrollHeight;
        });
      },
      disconnect() {
        if (this.stompClient && this.stompClient.connected) {
          this.stompClient.unsubscribe(`/topic/1`);
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
    margin-bottom: 10px;
  }

  .chat-message {
    margin-bottom: 10px;
  }

  .sent {
    text-align: right;
  }

  .received {
    text-align: left;
  }
</style>
