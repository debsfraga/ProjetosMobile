# Guia do professor — Rango na Régua (Semana 5)

Uso interno. Não distribua junto com o projeto dos alunos: as respostas das
sabotagens estão aqui.

---

## 1. Preparação antes da aula

- Abra o projeto e rode uma vez no emulador que você vai usar. Sincronização de
  Gradle na frente da turma consome dez minutos que não voltam.
- Substitua os lugares do `Catalogo.java` por estabelecimentos reais perto do
  campus. Leva cinco minutos e muda o clima da aula.
- Confira a internet do laboratório contra `brasilapi.com.br`. Se o proxy da
  instituição bloquear, a prévia cai no estado de erro — o que não é
  desastre, é a demonstração do estado de erro. Mas é melhor saber antes.
- Deixe o Logcat filtrado e visível no projetor desde o começo.

---

## 2. Onde cada demonstração entra na apresentação

| Slide | Momento | O que fazer no projeto |
|---|---|---|
| 4 — o jeito ingênuo | Bloco 1 | Escreva ao vivo um `for` com 2000 `addView` num ScrollView e rode. O engasgo convence mais do que o slide |
| 5 — a reciclagem | Bloco 1 | Só conversa. Não abra o Android Studio |
| 11 — layout do item | Bloco 2 | Troque a altura do `item_lugar.xml` para `match_parent` e rode. Um item ocupando a tela toda |
| 13 — o Adapter | Bloco 2 | Digite o Adapter junto com a turma. Use Alt+Enter → *Implement methods* |
| 14 — regra de ouro | Bloco 2 | Sabotagem A, abaixo |
| 15 — plugando | Bloco 2 | Comente o `setLayoutManager` e rode: tela em branco, sem erro nenhum |
| 18 — LayoutManagers | Bloco 3 | Alterne lista ↔ grade pelo menu. É o momento de maior efeito da aula |
| 19 — notify | Bloco 3 | Vote num lugar do meio até ele passar o líder |
| 21 — swipe | Bloco 3 | Arraste um item e desfaça pelo Snackbar |
| 22 — estado vazio | Bloco 3 | Filtre por uma categoria e remova os itens dela |
| 25 — prévia | Fechamento | Abra a prévia com internet, depois desligue o wi-fi e toque em *Tentar de novo* |

---

## 3. As três sabotagens da parte 4 da oficina

Peça que anotem o **sintoma** antes de você explicar a causa. O objetivo é
treinar leitura de sintoma, que é o que eles vão precisar durante o PI.

### Sabotagem A — a view reciclada chega suja

**Onde:** `LugarAdapter.LiderViewHolder.ligar()`

Apague o `else` do bloco da observação, deixando só:

```java
if (obs != null && !obs.trim().isEmpty()) {
    txtObservacao.setText(obs);
    txtObservacao.setVisibility(View.VISIBLE);
}
```

**Como provocar:** cadastre um lugar novo sem preencher o campo de observação
e vote nele até ele virar líder.

**Sintoma:** o card do líder mostra a observação do lugar que estava em
primeiro antes. Ninguém digitou aquilo.

**Causa:** a view do líder é reaproveitada. Sem o `else`, o texto antigo
continua ali. Todo atributo que o bind altera precisa dos dois caminhos.

---

### Sabotagem B — a posição velha

**Onde:** `LugarAdapter.LugarViewHolder.ligar()`

Troque a chamada do listener de clique para usar a lista por índice com a
`position` que chegou no bind, em vez do objeto:

```java
itemView.setOnClickListener(v -> ouvinte.aoClicarNoLugar(lista.get(position)));
```

(no projeto o listener já usa o objeto `lugar` direto, que é o jeito seguro —
para a sabotagem, faça o Adapter guardar a posição e usá-la depois)

**Como provocar:** remova dois ou três itens do topo com swipe, **sem**
desfazer, e depois toque num item do meio.

**Sintoma:** abre o detalhe de outro lugar.

**Causa:** a `position` é do momento do bind. Depois de uma remoção ela fica
desatualizada. Dentro de listener, sempre `getBindingAdapterPosition()`, ou
capture o objeto em vez do índice.

---

### Sabotagem C — avisar antes de mexer

**Onde:** `MainActivity.configurarSwipe()`, dentro do `onSwiped`

Inverta as duas linhas:

```java
adapter.notifyItemRemoved(posicao);
final Lugar removido = visiveis.remove(posicao);
```

**Como provocar:** arraste qualquer item.

**Sintoma:** o app cai. No Logcat aparece `IndexOutOfBoundsException` ou
`Inconsistency detected. Invalid item position`.

**Causa:** a RecyclerView pergunta o `getItemCount()` logo depois do aviso.
Como a coleção ainda não mudou, a conta não bate. Primeiro mexe na coleção,
depois avisa. Sempre nessa ordem.

---

### Sabotagem bônus, se sobrar tempo

Apague o `onSaveInstanceState` da `MainActivity` e gire o celular. Todos os
votos da turma somem. É o gancho para lembrar que estado de tela ainda é
responsabilidade nossa na mão — e que isso muda na semana 17, com ViewModel.

---

## 4. Perguntas que a turma costuma fazer

**"Por que o Adapter é declarado com `RecyclerView.ViewHolder` genérico?"**
Porque a lista tem dois layouts de item. Com um tipo só, dá para declarar o
ViewHolder específico e evitar o `instanceof` do `onBindViewHolder`.

**"Dá para arrastar e reordenar também?"**
Dá, é o `onMove` do mesmo `ItemTouchHelper`. Aqui ele devolve `false` de
propósito: quem manda na ordem é a votação, não o dedo.

**"Por que os votos somem quando eu fecho o app?"**
Porque não existe persistência ainda. Semana 8.

**"Não seria melhor usar DiffUtil?"**
Seria, e é o caminho quando a lista inteira chega pronta de fora, o que começa
na semana 6. Com alterações item a item feitas pela própria tela, o `notify`
específico é mais direto e ensina melhor o que está acontecendo.

**"Por que `Serializable` e não `Parcelable`?"**
`Parcelable` é mais rápido e é o recomendado em produção. `Serializable`
resolve com uma linha e não gasta aula com código repetitivo. Vale citar a
diferença e seguir.

**"Isso serve para o PI?"**
Serve e deve. Toda tela de listagem do Projeto Integrador sai daqui. Quem
esperar a semana de Firestore para começar a lista vai chegar apertado no
checkpoint.

---

## 5. Critério de correção sugerido para a oficina

| Item | Peso |
|---|---|
| Desenho do item no papel, antes do XML | 2,0 |
| `onBindViewHolder` completo, com os dois caminhos de cada atributo | 2,5 |
| Voto reordenando o ranking com `notifyItemMoved` | 2,5 |
| Estado vazio funcionando | 1,0 |
| Relatório das três sabotagens, com sintoma e causa | 2,0 |

O peso do desenho no papel pode subir para 3,0 se a turma estiver correndo
para o teclado antes de pensar — foi o que aconteceu na semana 3.
