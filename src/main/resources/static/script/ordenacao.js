function ordenar(n, num) {
    var table, rows, switching, i, x, y, shouldSwitch, dir, switchcount = 0;
    table = document.getElementById("data-table");
    switching = true;
    // Define a direção de ordenação como ascendente:
    dir = "asc";
    /* Faz um loop que irá continuar até que
    nenhuma troca seja feita: */
    while (switching) {
      // Start by saying: no switching is done:
      switching = false;
      rows = table.rows;
      /* Loop por todas as linhas da tabela (exceto a primeira,
      que contém headers): */
      for (i = 1; i < (rows.length - 1); i++) {
        // Start by saying there should be no switching:
        shouldSwitch = false;
        /* Pega os dois elementos que serão comprados
        a linha atual e a próxima: */
        x = rows[i].getElementsByTagName("TD")[n];
        y = rows[i + 1].getElementsByTagName("TD")[n];
        /* Check se as linhas devem trocar de lugar,
        baseado na direção, asc or desc: */
        if (num == true){
            var xnumero = x.innerHTML.replace(/\D/g, "");
            var ynumero = y.innerHTML.replace(/\D/g, "");
            if (xnumero.length == 8 && ynumero.length == 8){
                var xano = xnumero.slice(4)
                var yano = ynumero.slice(4)
                var xdia = xnumero.slice(0,2)
                var ydia = ynumero.slice(0,2)
                var xmes = xnumero.slice(2,4)
                var ymes = ynumero.slice(2,4)
                xnumero = xano + xmes + xdia
                ynumero = yano + ymes + ydia
                console.log(xnumero)
                console.log(ynumero)
            }
            if (dir == "asc") {
                if (Number(xnumero) > Number(ynumero)) {
                    //if so, mark as a switch and break the loop:
                    shouldSwitch = true;
                    break;
                } 
            } else if (dir == "desc") {
                if (Number(xnumero) < Number(ynumero)) {
                  // If so, mark as a switch and break the loop:
                  shouldSwitch = true;
                  break;
                }
              }
        } else if (num == false){
          //do something here
            if (dir == "asc") {
                if (x.innerHTML.toLowerCase() > y.innerHTML.toLowerCase()) {
                    // If so, mark as a switch and break the loop:
                    shouldSwitch = true;
                    break;
                }
            } else if (dir == "desc") {
                if (x.innerHTML.toLowerCase() < y.innerHTML.toLowerCase()) {
                    // If so, mark as a switch and break the loop:
                    shouldSwitch = true;
                    break;
                }
            }
      }
    }
      if (shouldSwitch) {
        /* Se shouldSwitch = true, realiza a troca e
        marca que a troca foi feita: */
        rows[i].parentNode.insertBefore(rows[i + 1], rows[i]);
        switching = true;
        // Cada vez que uma troca é realizada, aumenta a conta por 1:
        switchcount ++;
      } else {
        /* Se nenhuma troca foi feita e a direção é "asc",
        seta a direção para "desc" e roda o loop de novo. */
        if (switchcount == 0 && dir == "asc") {
          dir = "desc";
          switching = true;
        }
      }
    }
  }