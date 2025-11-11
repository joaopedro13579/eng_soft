#!/bin/bash

# ================================
# 🛑 Finalizador do app completo
# ================================

# Portas dos serviços
FRONTEND_PORT=3000
BACKEND_PORT=8080

echo "==============================="
echo "🛑 Encerrando aplicação completa"
echo "==============================="

# Função auxiliar para parar processos em uma porta específica
stop_port() {
  local PORT=$1
  local SERVICE_NAME=$2
  PID=$(lsof -ti:$PORT)

  if [ -n "$PID" ]; then
    echo "🧨 Encerrando $SERVICE_NAME (porta $PORT, PID $PID)..."
    kill -9 $PID
    echo "✅ $SERVICE_NAME encerrado!"
  else
    echo "ℹ️ Nenhum processo encontrado em $SERVICE_NAME (porta $PORT)"
  fi
}

# Parar backend e frontend
stop_port $BACKEND_PORT "Backend (Spring Boot)"
stop_port $FRONTEND_PORT "Frontend (Vue.js)"

echo ""
echo "==============================="
echo "✅ Todos os serviços foram encerrados."
echo "==============================="
