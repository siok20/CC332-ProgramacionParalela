#include <mpi.h>
#include <iostream>
#include <vector>

int main(int argc, char *argv[])
{
    MPI_Init(&argc, &argv);

    int world_rank;
    MPI_Comm_rank(MPI_COMM_WORLD, &world_rank);

    std::vector<int> send_buffer;

    constexpr int elements_per_proc = 1;
    int recv_buffer;

    // Initialize send_buffer only on the root process
    if (world_rank == 0)
    {
        send_buffer = {2, 3, 5, 7}; // Some arbitrary data
    }

    // Distribute the data among processes
    MPI_Scatter(send_buffer.data(), elements_per_proc, MPI_INT,
    &recv_buffer, elements_per_proc, MPI_INT, 0, MPI_COMM_WORLD);

    std::cout << "Process " << world_rank << " received " << recv_buffer << std::endl;

    MPI_Finalize();
    return 0;
}