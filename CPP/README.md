# Ejecutar con WSL

Instalar dependencias

```bash
sudo apt update
sudo apt install mpich
```

Compilar

```bash
mpic++ main.cpp -o main
```

Ejecutar
```bash
mpiexec -n 4 ./main
```