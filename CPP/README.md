# Ejecutar con WSL

Instalar dependencias

```bash
sudo apt update
sudo apt install mpich
```

Compilar

```bash
mpic++ first.cpp -o first
```
o

```bash
mpicxx -o first first.cpp
```

Ejecutar
```bash
mpiexec -n 4 ./first
```