import tabula as tb
import pandas as pd

#função para range de páginas do doc
def parse_range(astr):
    result=set()
    for part in astr.split(','):
        x=part.split('-')
        result.update(range(int(x[0]),int(x[-1])+1))
    return sorted(result)

#caminho arquivo e páginas
file = 'files\RENAME-2022.pdf'
pages=parse_range('80-145') #80-145 28-78
fc = 28.28

#Margens do arquivo - [top,left,bottom,width]
box=[3,1,21,27]
for i in range (0, len(box)):
    box[i]*=fc

#Criando dataframe com base no arquivo
dfRename = pd.DataFrame()
for page in pages:
    index = pages.index(page)
    dfRenameExtract = tb.read_pdf(file, pages=page,area=[box],output_format="dataframe", stream=True)
    dfRenameHeader = dfRenameExtract[0]
    dfRenameHeader.rename(columns={ dfRenameHeader.columns[0]: "RS_Nome" , dfRenameHeader.columns[1]: "RS_Composicao"}, inplace = True)
    dfRename = pd.concat([dfRename, dfRenameHeader])

#Delete de linhas vazias
dfRename = dfRename[dfRename['RS_Nome'].notna()]
dfRename = dfRename[dfRename["RS_Nome"] != "RS_Nome"]

#pegando apenas a coluna RS_Nome
dfRename = dfRename.loc[:, dfRename.columns.isin(['RS_Nome'])]
dfRename.head(30)

#criando arquivo .csv com base no dataframe
dfRename.to_csv('files\output-rename.csv')
