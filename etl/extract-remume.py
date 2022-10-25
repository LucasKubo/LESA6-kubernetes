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
file = 'files\REMUME-2022.pdf'
pages=parse_range('1-21')
fc = 28.28

#Margens do arquivo - [top,left,bottom,width]
box=[5,1,21,27]
for i in range (0, len(box)):
    box[i]*=fc

#Criando dataframe com base no arquivo
dfRemume = pd.DataFrame()
for page in pages:
    index = pages.index(page)
    dfExtract = tb.read_pdf(file, pages=page,area=[box],output_format="dataframe", stream=True)
    dfRemumeHeader = dfExtract[0]
    dfRemumeHeader.rename(columns={dfRemumeHeader.columns[1]: "RS_Nome-Remume"}, inplace = True)
    dfRemume = pd.concat([dfRemume, dfRemumeHeader])

#Delete de linhas vazias
dfRemume = dfRemume[dfRemume['RS_Nome-Remume'].notna()]
dfRemume = dfRemume[dfRemume["RS_Nome-Remume"] != "RS_Nome-Remume"]

#pegando apenas a coluna RS_Nome-Remume
dfRemume = dfRemume.loc[:, dfRemume.columns.isin(['RS_Nome-Remume'])]
dfRemume.head(30)

#criando arquivo .csv com base no dataframe
dfRemume.to_csv('files\output-remume.csv')

