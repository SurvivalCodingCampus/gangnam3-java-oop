/*package com.survivalcoding;

public class list2 {
    interface MyList<E> {
        void add(E e);

        void add(int index, E e);

        E get(int index);

        E set(int index, E e);

        E remove(int index);

        int size();

        boolean isEmpty();

        boolean contains(Object o);

        int indexOf(Object o);
    }

    public static class AbstractList<E> implements MyList<E> {
        @Override
        //size(),get(),add()만 사용
        //오버라이드 할때는 호출하는 놈을 덮어 씌울수도 있도 아니면 코드 자체를 덮어 씌울 수도 있다.
        public void add(E e) {
            add(size(), e);
        }
        //you dont need to put get set again cuase there exist from the import and all the stuff from ehre parents from the import;

    }
}
*/

