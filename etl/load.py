import psycopg2

#conexão com bd
teste="8e353835da73201b647472a28c5b2f74a14bcbf903218a33dbe3fd54bb3767c4"
conn = psycopg2.connect("host=ec2-3-213-66-35.compute-1.amazonaws.com dbname=d2denq0bnhvj5 user=hieekssetvcmlw password=" + teste)
cur = conn.cursor()

#pegando dados do arquivo e inputando na tabela listagem_remedios
with open('files\output-final.csv', 'r') as arquivo_final:
    next(arquivo_final)
    cur.copy_from(arquivo_final, 'listagem_remedios', sep=',')

#commitando no bd
conn.commit()