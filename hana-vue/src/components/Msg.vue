<template>
    <div class="msg-container">
        <div v-if="loading" class="loading">Loading...</div>
        <div v-if="error" class="error">{{ error }}</div>

        <!-- 消息列表 -->
        <ul class="message-list">
            <li v-for="msg in messages" :key="msg.id">
                <strong>消息来自:{{ userStore.userInfo.name }}</strong>
                <p>{{ msg.content }}</p>
                <span class="time">{{ formatDate(msg.time) }}</span>
            </li>
        </ul>

        <!-- 发送消息 -->
        <div class="send-message">
            <input v-model="recipientId" type="number" placeholder="接受消息者的ID" />
            <textarea v-model="messageContent" placeholder="输入你的消息"></textarea>
            <button @click="sendMessage">发送消息</button>
        </div>
    </div>
</template>

<script setup>
import { ref, onMounted, watch } from 'vue';
import { useMsgStore } from '../store/msgStore';
import { storeToRefs } from 'pinia';
import Header from '../views/HeaderView.vue';
import { ElMessage } from "element-plus";
import { useUserStore } from '../store/userStore';

const msgStore = useMsgStore();
const { messages, loading, error, userId } = storeToRefs(msgStore);
const recipientId = ref('');
const messageContent = ref('');
const userStore = useUserStore();


// 监听 userId 的变化
watch(userId, (newId) => {
    if (newId) {
        msgStore.fetchMessages(); // 当 userId 有值时，再获取消息列表
    }
});

const sendMessage = () => {
    const currentUserId = msgStore.userId.value; // 获取当前用户ID

    // 检查是否填写了接受者ID和消息内容
    if (!recipientId.value || !messageContent.value) {
        ElMessage({
            message: '请输入完整内容！！',
            type: 'warning',
        });
        return; // 如果内容不完整，则直接返回
    }

    // 检查是否尝试发送给自己
    if (recipientId.value == currentUserId) {
        ElMessage({
            message: '不能给自己发送消息！',
            type: 'error',
        });
        recipientId.value = ''; // 清空接收者ID
        return; // 如果发送给自己，则直接返回
    }

    // 发送消息
    msgStore.sendMessage(recipientId.value, messageContent.value);

    // 清空输入框
    recipientId.value = '';
    messageContent.value = '';
};

const formatDate = (dateStr) => {
    const date = new Date(dateStr);
    return date.toLocaleString();
};
</script>

<style scoped>
.msg-container {
    max-width: 800px;
    margin: 0 auto;
    padding: 20px;
    background-color: #f4f6f9;
    border-radius: 8px;
    box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
}

.loading,
.error {
    text-align: center;
    margin: 10px 0;
    font-size: 18px;
    font-weight: bold;
}

.message-list {
    list-style: none;
    padding: 0;
    margin-bottom: 20px;
    max-height: 400px;
    overflow-y: auto;
    background-color: #fff;
    border-radius: 8px;
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.message-list li {
    display: flex;
    flex-direction: column;
    padding: 15px;
    margin: 10px;
    background-color: #e1f5fe;
    border-radius: 8px;
    border: 1px solid #b3e5fc;
    position: relative;
}

.message-list li::before {
    content: '';
    position: absolute;
    top: 15px;
    left: -8px;
    width: 0;
    height: 0;
    border: 8px solid transparent;
    border-right-color: #e1f5fe;
}

.message-list li .time {
    font-size: 12px;
    color: #999;
    margin-top: 10px;
}

.send-message {
    display: flex;
    flex-direction: column;
    margin-top: 20px;
}

.send-message input,
.send-message textarea {
    width: 100%;
    padding: 10px;
    margin-bottom: 10px;
    border: 1px solid #ccc;
    border-radius: 4px;
    font-size: 14px;
    outline: none;
    transition: border-color 0.3s;
}

.send-message input:focus,
.send-message textarea:focus {
    border-color: #42b983;
}

.send-message button {
    padding: 10px;
    background-color: #42b983;
    color: #fff;
    border: none;
    border-radius: 4px;
    cursor: pointer;
    font-size: 16px;
    transition: background-color 0.3s;
}

.send-message button:hover {
    background-color: #37a572;
}

/* 响应式设计 */
@media (max-width: 600px) {
    .msg-container {
        padding: 10px;
    }

    .message-list li {
        padding: 10px;
    }

    .send-message button {
        font-size: 14px;
    }
}
</style>