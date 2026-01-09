# CentroDistribuicaoPCO

<img width="1650" height="806" alt="image" src="https://github.com/user-attachments/assets/7e990e31-5e80-4b7c-8e72-318e40182b93" />


<img width="1650" height="931" alt="image" src="https://github.com/user-attachments/assets/eb7a1e41-df8a-4c1a-8baf-aede8b3e1e86" />

### Perguntas para ver:

- Codigo na loja tem tamanho fixo? se tiver tem de verificar depois.

### TODO:






Pessoa A (Domínio/Core) , Pessoa B (Suporte/UI),  com merges diários + daily standup (10min)

DIA 0 (Hoje, 30min JUNTOS)
[ ] Pessoa A+B: JSON Loader em Main (parse Gson para todos handlers) + classes domain básicas (Encomenda, Expedicao)



(Definir enums básicos:
  - EstadoStock  
  - EstadoEncomenda ta
  - EstadoExpedicao 
  - TipoRestricao ta 
  - TipoLocalizacao  ta 
  - PapelUtilizador ta )  

  FEITO


 

DIA 1 (Amanhã)
Pessoa A (Fluxo Principal - 3h):
- InventarioHandler (UC11/15, integra Rececao já feita) [file:9][file:12]
- EncomendaHandler (UC09 reserva FIFO) [file:2]

(
No EncomendaHandler (UC09), garantir logo: quase feito (falta a reserva fifo)

- Reserva ≠ consumo de stock

- Estrutura clara:

- StockDisponivel

- StockReservado)


Pessoa B (Suporte - 3h):
- FornecedorHandler CRUD (UC03/19-20) [file:10]- Feito 
- LojaHandler CRUD (UC22) [file:8]- Feito 
Merge: Teste receção → stock → encomenda simples




DIA 2
Pessoa A (Exp. Core - 3h):
- ExpedicaoHandler (UC10/12, use Tarefa) [file:5][file:7]
Pessoa B (Gestão - 3h):
- Ajustes stock + aprovações (UC13/16)
Merge: Fluxo completo Receção → Expedição!

(No ExpedicaoHandler:

- Criar desde logo:

- classe Tarefa feito

- histórico simples de movimentações) 


DIA 3
Pessoa A (Autenticação - 2h):
- Refatore Autenticacao + menus roles básicos (UC01/02) [file:6]
Pessoa B (CRUD Final - 2h):
- Gerir localizações (UC23) -> Feito  
Merge: Menus funcionais por role


DIA 4 (Final)
Pessoa A+B (1h cada):
- Relatórios UC14 + testes anomalias (stock neg., NC)
- Relatório PDF + git clean
