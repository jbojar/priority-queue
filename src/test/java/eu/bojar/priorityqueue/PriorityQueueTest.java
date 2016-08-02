package eu.bojar.priorityqueue;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.*;

public class PriorityQueueTest {


  @DataProvider
  public static Object[][] leftChildren() {
    return new Object[][]{
            {0, 1},
            {1, 3},
            {2, 5},
            {3, 7},
            {4, 9},
            {5, 11},
            {6, 13}
    };
  }

  @DataProvider
  public static Object[][] rightChildren() {
    return new Object[][] {
            {0, 2},
            {1, 4},
            {2, 6},
            {3, 8},
            {4, 10},
            {5, 12},
            {6, 14}
    };
  }

  @DataProvider
  public static Object[][] parents() {
    return new Object[][]{
            {1, 0},
            {2, 0},
            {3, 1},
            {4, 1},
            {5, 2},
            {6, 2}
    };
  }

  @Test(dataProvider = "leftChildren")
  public void testLeftChild(int p, int lc) throws Exception {
    // given
    PriorityQueue<String, Integer> pq = new PriorityQueue<String, Integer>(32);

    // when & then
    assertThat(pq.leftChild(p)).as("left child of %d should be %d", p, lc).isEqualTo(lc);
  }

  @Test(dataProvider = "rightChildren")
  public void testRightChild(int p, int rc) throws Exception {
    // given
    PriorityQueue<String, Integer> pq = new PriorityQueue<String, Integer>(32);

    // when & then
    assertThat(pq.rightChild(p)).as("right child of %d should be %d", p, rc).isEqualTo(rc);
  }

  @Test(dataProvider = "parents")
  public void testParent(int c, int p) throws Exception {
    // given
    PriorityQueue<String, Integer> pq = new PriorityQueue<String, Integer>(32);

    // when & then
    assertThat(pq.parent(c)).as("parent of %d should be %d", c, p).isEqualTo(p);
  }

  @Test
  public void testAdd_multiple() throws Exception {
    // given
    PriorityQueue<String, Integer> pq = new PriorityQueue<String, Integer>(64);
    List<Integer> input = IntStream.range(0, 64).boxed().collect(Collectors.toList());
    Collections.shuffle(input);

    // when & then
    for (int i = 0; i < input.size(); i++) {
      int e = input.get(i);
      pq.add(Integer.toString(e), e);

      assertThat(pq.size()).isEqualTo(i + 1);
      assertPriorityQueueStructure(pq);
    }
  }

  @Test
  public void testNodeRemove() throws Exception {
    // given
    PriorityQueue<String, Integer> pq = new PriorityQueue<String, Integer>(64);
    List<Integer> input = IntStream.range(0, 64).boxed().collect(Collectors.toList());
    Collections.shuffle(input);

    final List<PriorityQueue<String, Integer>.Element> nodes = input.stream().map(e -> pq.add(Integer.toString(e), e)).collect(Collectors.toList());

    // sanity check
    assertPriorityQueueStructure(pq);

    // when
    IntStream.range(0, 10).forEach(i -> {
      int idxToRemove = ThreadLocalRandom.current().nextInt(nodes.size());
      PriorityQueue<String, Integer>.Element nodeToRemove = nodes.remove(idxToRemove);

      nodeToRemove.remove();
      assertThat(nodeToRemove.removed).isTrue();
      assertThat(pq.size()).isEqualTo(64 - i - 1);
      assertPriorityQueueStructure(pq);
    });

  }

  @Test
  public void testAdd_overflow() throws Exception {
    // given
    PriorityQueue<String, Integer> pq = new PriorityQueue<String, Integer>(1);
    pq.add("2", 2);

    // when
    pq.add("1", 1);

    // then
    assertThat(pq.size()).isEqualTo(2);
    assertThat(pq.peek().priority()).isEqualTo(1);
    assertThat(pq.peek().value()).isEqualTo("1");
  }

  @Test
  public void testRemove_multiple() throws Exception {
    // given
    PriorityQueue<String, Integer> pq = new PriorityQueue<String, Integer>(64);
    List<Integer> input = IntStream.range(0, 64).boxed().collect(Collectors.toList());
    Collections.shuffle(input);

    input.forEach(e -> pq.add(Integer.toString(e), e));

    // sanity check
    assertPriorityQueueStructure(pq);

    // when
    List<PriorityQueue<String, Integer>.Element> dequeued = new ArrayList<>(64);
    while (pq.size() > 0) {
      dequeued.add(pq.remove());
      assertPriorityQueueStructure(pq);
    }

    // then
    assertThat(dequeued).isSortedAccordingTo((e1, e2) -> e1.priority().compareTo(e2.priority()));
    assertThat(dequeued).allMatch(n -> n.removed);
    assertThat(dequeued).allMatch(node -> node.value().equals(Objects.toString(node.priority())));
  }

  @Test
  public void testRemove_emptyHeap() throws Exception {
    // given
    PriorityQueue<String, Integer> pq = new PriorityQueue<String, Integer>(8);
    pq.add("1", 1);
    pq.remove();

    // when & then
    assertThatThrownBy(pq::remove).isInstanceOf(IllegalStateException.class).hasMessageContaining("PriorityQueue is empty");
  }

  @Test
  public void testChangePriority_decrease() {
    // given
    PriorityQueue<String, Integer> pq = new PriorityQueue<String, Integer>(10);
    Map<String, PriorityQueue<String, Integer>.Element> elements = IntStream.range(1, 11).mapToObj(i -> pq.add("e" + i, i)).collect(Collectors.toMap(e -> e.value(), e -> e));

    // sanity check
    assertThat(pq.peek()).isEqualTo(elements.get("e1"));

    // when
    elements.get("e10").changePriority(0);

    // then
    assertThat(pq.size()).isEqualTo(10);
    assertThat(pq.peek().value()).isEqualTo("e10");
    assertPriorityQueueStructure(pq);
  }

  @Test
  public void testChangePriority_increase() {
    // given
    PriorityQueue<String, Integer> pq = new PriorityQueue<String, Integer>(10);
    Map<String, PriorityQueue<String, Integer>.Element> elements = IntStream.range(1, 11).mapToObj(i -> pq.add("e" + i, i)).collect(Collectors.toMap(e -> e.value(), e -> e));

    // sanity check
    assertThat(pq.peek()).isEqualTo(elements.get("e1"));

    // when
    elements.get("e1").changePriority(11);

    // then
    assertThat(pq.size()).isEqualTo(10);
    assertThat(pq.peek().value()).isEqualTo("e2");
    assertPriorityQueueStructure(pq);
  }

  private void assertPriorityQueueStructure(PriorityQueue<?, Integer> pq) {
    if (pq.size() > 0) {
      final int headNodeIdx = pq.tree.get(0).idx;
      assertThat(headNodeIdx).as("Head node must have index 0 but has index %d", headNodeIdx).isEqualTo(0);
    }
    for (int h = pq.size() - 1; h > 0; h--) {
      assertThat(pq.tree.get(h).idx).as("Element index %d should be equal to its index in array %d", pq.tree.get(h).idx, h).isEqualTo(h);
      assertThat(pq.tree.get(pq.parent(h)).priority()).as("Corrupted heap structure: %s", pq.toString()).isLessThanOrEqualTo(pq.tree.get(h).priority());
    }
  }
}