from re import sub
import pandas as pd
import numpy as np
from unicodedata import normalize

#leitura arquivo
dfRename = pd.read_csv ('files\output-rename.csv')
dfRemume = pd.read_csv ('files\output-remume.csv')

#setando index dos dataframes
dfRename.set_index(['RS_Nome'])
dfRemume.set_index(['RS_Nome-Remume'])

#retirando acentos
dfRename['RS_Nome']=dfRename['RS_Nome'].str.normalize('NFKD').str.encode('ascii', errors='ignore').str.decode('utf-8')
dfRemume['RS_Nome-Remume']=dfRemume['RS_Nome-Remume'].str.normalize('NFKD').str.encode('ascii', errors='ignore').str.decode('utf-8')

#transformando dataframe em lista
lista_remume=dfRemume.values.tolist()
lista_rename=dfRename['RS_Nome'].values.tolist()

#criando lista de elementos para serem trocados
list_replace=['(','+','*','.','+','mg','ml','1','2','3','4','5','6','7','8','9','0','\)','\"','\'',',']

#fazendo replace nos elementos da list_replace
for r in list_replace:
    dfRename['RS_Nome']=dfRename['RS_Nome'].str.replace(r,"")

#lista de linhas para serem excluidas
string_delete=["ANEXO","Denominação","Composição","atenuada","Uncaria","Willd","500 unidades","humano","Denominacao Concentracao/ Forma","generica Composicao farmaceutica"]
list_string_delete="|".join(str(f) for f in string_delete)

#excluindo linhas de acordo com list_string_delete
dfRename=dfRename[dfRename['RS_Nome'].str.contains(list_string_delete) == False]

#concatenando dfRename e dfRemume
dfResult = pd.concat([dfRename, dfRemume], axis=1, join='outer')

#criando lista rename para função contais
list_rename_contains="|".join(str(e) for e in lista_rename)
list_rename_contains= list_rename_contains.replace("/",'').replace(",", '').replace("*",'').replace(".",'').replace("+",'').replace("mg","").replace("ml","")

#verificando a disponibilidade dos medicamentos do rename no dataframe do remume
dfRename['RS_DisponivelSus']=dfResult['RS_Nome-Remume'].str.contains(list_rename_contains, regex=True)

#pegando apenas a coluna RS_Nome e RS_DisponivelSus
dfRename.set_index(['RS_Nome'])
dfRename = dfRename.loc[:, dfRename.columns.isin(['RS_Nome','RS_DisponivelSus'])]

#Delete de linhas vazias
dfRename['RS_DisponivelSus'].fillna(False, inplace=True)
dfRename=dfRename.dropna(axis=0, subset=['RS_Nome', 'RS_DisponivelSus'])
dfRename = dfRename[dfRename['RS_Nome'].notna()]
dfRename = dfRename[dfRename["RS_Nome"] != "RS_Nome"]

#criando arquivo .csv com base no dataframe
dfRename.to_csv('files\output-final.csv')




