# danilo-teodoro-mobile-engineer

* Para o desenvolvimento foi utilizado o padrão MVI (Model View Intent). Padrão em que a view se comunica com  ViewModel atravéz de intenções.

Foram utilizadas as seguintes bibliotecas:

1. jetpack lifecycle: (ViewModel e LiveData). Facilita bastante o tratamento de estado dos objetos e controle do ciclo de vida da aplicação.
2. Hit: Framework utilizado para injeçao de dependências. ajuda a evitar o acoplamento dando mais flexibilidade para a aplicação.
3. Coroutines : Utilizado para executar tarefas em background (como requisiçoes Http). Veio para substitur o 
AsyncTask.

4. Retrofit: Utilizado para realizar requisições HTTP.

5. Gson: Framework Utilizado para Serialização e Deserialização de objetos JSON.


# Organizaçao dos Pacotes

A aplicação foi desenvolvida seguindo o principio separação de responsabilidade. A aplicaçao possui um dominio, e cada pacote possui seu subominio.

model:       representa o domínio da aplicação.

network:     classes e interfaces responsáveis para comunicar com o servido via REST.

di:          modulos para injeção de dependências.

repository:  classes que fornece os dados e faz a ponte entre os ViewModels e a origem dos dados.

ui:          contém os viewmodels, activitys, fragments e adapters

util:        classes e objetos utilitários

# Estilos e Layout

O app foi desenvolvido para se adaptar ao tema Dark e ao tema padrão.
O app ajusta conforme o tamanho das telas e orientação.