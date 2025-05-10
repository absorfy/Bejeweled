import SockJS from 'sockjs-client';
import { Client } from '@stomp/stompjs';
import { useEffect } from 'react';

const socketUrl = 'http://localhost:8080/ws';

export default function useCommentSocket(onNewComment) {
  useEffect(() => {
    const client = new Client({
      webSocketFactory: () => new SockJS(socketUrl),
      onConnect: () => {
        client.subscribe('/topic/comments', (message) => {
          const newComment = JSON.parse(message.body);
          onNewComment(newComment);
        });
      },
    });

    client.activate();
    return () => client.deactivate();
  }, [onNewComment]);
}
