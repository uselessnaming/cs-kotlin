package tree

class RedBlackTree {
    private data class Node(
        var type: Color,
        val leftChild: Node,
        val rightChild: Node
    )

    private enum class Color {
        Red, Black
    }
}