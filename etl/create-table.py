import psycopg2

#conexão com bd
conn = psycopg2.connect("host=ec2-3-213-66-35.compute-1.amazonaws.com dbname=d2denq0bnhvj5 user=hieekssetvcmlw password=8e353835da73201b647472a28c5b2f74a14bcbf903218a33dbe3fd54bb3767c4")
cur = conn.cursor()

#criando tabela
cur.execute("""CREATE TABLE listagem_remedios(
    rs_id SERIAL PRIMARY KEY,
    rs_nome text,
    rs_disponivel_sus boolean
)
""")

#commitando no bd
conn.commit()