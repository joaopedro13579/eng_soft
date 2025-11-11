#!/bin/bash

# ================================
# 🚀 Inicializador completo do app
# ================================

# Caminhos (ajuste conforme seu projeto)
FRONTEND_DIR="$HOME/trabalhos/eng_soft/Front/nuxt-app"
BACKEND_DIR="$HOME/trabalhos/eng_soft/demo"

# Porta padrão do backend
BACKEND_PORT=8080

# Função para verificar se o backend já está rodando
check_backend() {
  if lsof -i:$BACKEND_PORT >/dev/null 2>&1; then
    echo "⚙️  Backend já está rodando na porta $BACKEND_PORT"
    return 0
  else
    return 1
  fi
}

echo "==============================="
echo "🌐 Iniciando aplicação completa"
echo "==============================="

# Iniciar backend (Spring Boot)
cd "$BACKEND_DIR" || exit
if check_backend; then
  echo "➡️  Pulando backend (já em execução)"
else
  echo "🚀 Iniciando backend (Spring Boot)..."
  ./mvnw spring-boot:run > backend.log 2>&1 &
  echo "✅ Backend iniciado (log: backend.log)"
fi

# Iniciar frontend (Vue)
cd "$FRONTEND_DIR" || exit
echo "🚀 Iniciando frontend (Vue.js)..."
npm run dev > frontend.log 2>&1 &
echo "✅ Frontend iniciado (log: frontend.log)"

echo ""
echo "==============================="
echo "✅ Tudo pronto!"
echo "📦 Backend: http://localhost:$BACKEND_PORT"
echo "💻 Frontend: http://localhost:3000"
echo "==============================="
