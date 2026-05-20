# Serviço Auditor de Falhas (DLQ)

## Por que escolhi Arquitetura Hexagonal

Antes de definir a arquitetura, pensei no que esse serviço realmente faz. Ele não possui interface, não expõe endpoints de negócio e nem trabalha com fluxo HTTP tradicional. É basicamente um *worker* que consome mensagens da DLQ, aplica uma regra de severidade e salva o resultado no banco.

Por isso, MVC não fazia muito sentido. Não existe View nem Controller HTTP aqui, então eu acabaria forçando conceitos que não representam o funcionamento real do sistema.

Layered chegou mais perto, mas ainda me incomodava pelo acoplamento natural entre `Service` e `JpaRepository`. A regra de negócio acabaria dependendo diretamente do Spring Data, dificultando testes isolados e futuras trocas de persistência.

A Arquitetura Hexagonal resolveu melhor esse cenário por alguns motivos:

- separa claramente regra de negócio de detalhe técnico;
- permite trocar entrada ou persistência sem impactar o núcleo;
- facilita testes sem subir Spring ou banco;
- deixa as dependências organizadas de forma mais previsível.

Hoje a entrada é SQS, mas amanhã poderia existir um endpoint REST, um job ou qualquer outra forma de consumo reutilizando o mesmo caso de uso.

---

## Organização das pastas

```text
src/main/java/com/example/auditor
├── domain/          ← regras e modelos de negócio
├── application/     ← casos de uso e portas
└── adapter/         ← integrações externas (SQS, JPA etc.)
```

### `domain/`
Contém o núcleo da aplicação, sem dependência de framework.

```text
domain/
├── model/
│   ├── FailedMessage.java
│   ├── Severity.java
│   └── AuditStatus.java
└── service/
    └── SeverityPolicy.java
```

### `application/`
Camada responsável pelos casos de uso e contratos da aplicação.

```text
application/
├── port/
│   ├── in/
│   │   └── AuditFailedMessageUseCase.java
│   └── out/
│       └── FailedMessageRepositoryPort.java
└── service/
    └── AuditFailedMessageService.java
```

### `adapter/`
Implementações concretas que conversam com infraestrutura.

```text
adapter/
├── in/messaging/
│   └── DlqSqsListener.java
└── out/persistence/
    ├── FailedMessageEntity.java
    ├── SpringDataFailedMessageRepository.java
    └── FailedMessagePersistenceAdapter.java
```

---

## Algumas decisões importantes

### Separação entre domínio e JPA

`FailedMessage` e `FailedMessageEntity` são classes diferentes de propósito.

Isso evita acoplamento do domínio com JPA e mantém a regra de negócio independente da tecnologia de persistência.

---

### `SeverityPolicy` como classe estática

A classe apenas recebe um valor e retorna uma severidade. Como não possui estado nem dependências, transformar isso em bean do Spring seria desnecessário.

---

### Garantia de persistência antes do ACK

O `@SqsListener`, no modo padrão (`ON_SUCCESS`), só remove a mensagem da fila se o processamento terminar sem exceção.

Como o método é transacional, qualquer falha no `save()` impede o ACK e a mensagem retorna para a fila automaticamente.

---

### Payload tratado como `String`

O banco já exige o payload bruto como texto, então criar DTO completo seria excesso.

O parse é feito apenas quando necessário usando `JsonNode`, deixando a leitura mais flexível para futuras mudanças no payload.

---

### Uso do H2

Escolhi H2 porque o foco da atividade é arquitetura, não infraestrutura.

Assim, o projeto roda sem instalação adicional e continua desacoplado do banco utilizado.