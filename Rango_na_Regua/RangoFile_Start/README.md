# Rango na Régua

Ranking gastronômico da turma. Cada dupla indica lugares para comer perto do
campus, todo mundo vota, e o ranking se reorganiza na hora.

Projeto de demonstração da **Semana 5 — RecyclerView, Adapter e ViewHolder**.
Desenvolvimento de Aplicativos Móveis, 4º semestre de ADS, UniSENAI SP —
Campus São Caetano do Sul.

---

## Como abrir

1. Descompacte a pasta e abra no Android Studio por **File → Open**, apontando
   para a raiz do projeto (a pasta que contém o `settings.gradle`).
2. O projeto não traz o `gradlew` nem o `gradle-wrapper.jar`, só o
   `gradle-wrapper.properties`. Na primeira sincronização o Android Studio
   pergunta se pode criar o wrapper. Aceite e espere o download.
3. Emulador ou celular com **Android 7.0 (API 24)** ou superior.
4. A tela de prévia da semana 6 precisa de internet. O resto funciona offline.

Sem dependência de conta, chave de API ou arquivo de configuração. Nada de
credencial no código.

---

## O que cada arquivo ensina

| Arquivo | Conceito da aula |
|---|---|
| `model/Lugar.java` | Modelo que só guarda dado. `Serializable` para viajar em Intent e sobreviver à rotação |
| `data/Catalogo.java` | Dados iniciais e a ordenação do ranking. Tudo em memória |
| `adapter/LugarAdapter.java` | Os três métodos obrigatórios, ViewHolder estático, dois tipos de item, clique por interface |
| `MainActivity.java` | LayoutManager, `notify` específico, swipe, filtro, estado vazio, `onSaveInstanceState` |
| `DetalheActivity.java` | Recebe o objeto por Intent (semana 4) |
| `NovoLugarActivity.java` | Devolve o objeto pela Result API (semana 4) |
| `res/layout/item_lugar.xml` | Layout de **um** item, com a armadilha do `match_parent` comentada |
| `res/layout/item_lider.xml` | O segundo tipo de item, escolhido pelo `getItemViewType` |
| `previa/` | Prévia da semana 6 — leia a seção abaixo |

---

## O que este projeto deliberadamente **não** usa

Nada aqui está fora do que já foi ensinado até a semana 5.

- **Sem ViewModel, LiveData ou MVVM.** Isso é semana 17. O estado da tela mora
  em campos da `MainActivity` e sobrevive à rotação pelo `onSaveInstanceState`,
  do mesmo jeito que no Rolê da Turma da semana 4.
- **Sem permissões em tempo de execução.** Semana 15. A permissão `INTERNET`
  do manifesto é permissão normal: o sistema concede na instalação e não existe
  diálogo para o usuário.
- **Sem banco de dados.** Persistência é semana 8. Feche o app e o ranking volta
  ao estado inicial. Avise a turma antes que alguém abra chamado de bug.
- **Sem Retrofit, Gson ou Glide.** A prévia da semana 6 usa `HttpURLConnection`
  e `org.json`, que já vêm no Android, justamente para que depois fique visível
  o que a biblioteca poupa.

---

## A prévia da semana 6

A tela `PreviaSemana6Activity` (menu da Toolbar → *Prévia da semana 6*) busca os
feriados nacionais do ano corrente na [BrasilAPI](https://brasilapi.com.br) e
mostra quanto falta para cada um.

```
GET https://brasilapi.com.br/api/feriados/v1/2026

[ { "date": "2026-01-01", "name": "Confraternização mundial" }, ... ]
```

API pública, sem chave e sem cadastro.

Compare o `FeriadoAdapter` com o `LugarAdapter`: a estrutura é idêntica. A
origem do dado não muda uma linha do Adapter, do ViewHolder ou do
LayoutManager. O que aparece de novo é o que a semana 6 vai tratar:

1. rede não roda na thread principal — `ExecutorService` para buscar, `Handler`
   para voltar e mexer na interface;
2. JSON vira objeto Java na mão, com `JSONArray` e `JSONObject`;
3. a tela tem três estados, não um: carregando, deu certo, deu ruim.

Para ver o estado de erro em sala, desligue o wi-fi do emulador e toque em
*Tentar de novo*.

---

## Antes da aula

Troque os oito lugares do `Catalogo.java` pelas lanchonetes reais perto do
campus. O ranking rende muito mais discussão quando a turma reconhece os
endereços.

---

## Roteiro rápido de demonstração

1. Abrir o app e rolar a lista.
2. Votar num lugar do meio até ele passar o líder, mostrando a animação.
3. Alternar lista ↔ grade pelo menu, sem tocar no Adapter.
4. Arrastar um item para o lado e desfazer pelo Snackbar.
5. Filtrar por uma categoria e voltar para *Todas*.
6. Filtrar por uma categoria sem itens para ver o estado vazio.
7. Girar o celular e mostrar que os votos continuam lá.
8. Abrir a prévia da semana 6, com e sem internet.

O passo a passo detalhado, com as sabotagens sugeridas, está no
`GUIA-DO-PROFESSOR.md`.
