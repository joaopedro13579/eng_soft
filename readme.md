[<head>
  <meta charset="UTF-8" />
  <meta name="viewport" content="width=device-width, initial-scale=1.0" />
  <title>ENG_SOFT - Projeto Java Spring</title>
  <style>
    body {
      font-family: Arial, sans-serif;
      background-color: #f8f9fa;
      color: #2c3e50;
      padding: 40px;
      line-height: 1.6;
    }

    h1 {
      font-size: 2rem;
      margin-bottom: 10px;
    }

    h2 {
      font-size: 1.5rem;
      margin-top: 40px;
      margin-bottom: 10px;
    }

    h3 {
      font-size: 1.2rem;
      margin-top: 30px;
      color: #007BFF;
    }

    ul {
      margin-left: 20px;
    }

    pre {
      background-color: #fff;
      border: 1px solid #ccc;
      padding: 16px;
      font-family: monospace;
      white-space: pre;
      overflow-x: auto;
      margin-top: 10px;
    }

    code {
      background-color: #f1f1f1;
      padding: 2px 4px;
      font-family: monospace;
      border-radius: 4px;
    }

    em {
      color: #555;
    }

    a {
      color: #007BFF;
      text-decoration: none;
    }

    a:hover {
      text-decoration: underline;
    }
  </style>
</head>
<body>

  <h1>📘 ENG_SOFT</h1>

  <p>
    Projeto de exemplo em Java/Spring Boot que implementa operações CRUD para entidades como <code>Project</code> (e possivelmente <code>Message</code>, <code>User</code>, etc.), com autenticação básica via token.
  </p>

  <h2>⚙️ Tecnologias utilizadas</h2>
  <ul>
    <li>Java</li>
    <li>Spring Boot</li>
    <li>PostgreSQL</li>
    <li>JDBC (uso de <code>DriverManager</code> e <code>PreparedStatement</code>)</li>
    <li>Spring Web (controladores REST)</li>
    <li>Spring Dependency Injection (<code>@Autowired</code>)</li>
  </ul>

  <h3><a href="#">📄 Arquivo de Instalação</a></h3>
  <p><em>(Adicione o link ou instruções aqui)</em></p>

  <h2>📁 Estrutura do Projeto</h2>
  <pre>
📁 src/
└── 📁 main/
    ├── 📁 java/
    │   └── 📁 com/
    │       └── 📁 example/
    │           └── 📁 demo/
    │               ├── 📁 controllers/
    │               ├── 📁 services/
    │               ├── 📁 entities/
    │               └── 📁 authenticators/
    └── 📁 resources/
