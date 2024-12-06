package edu.uncc.assessment06;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

import edu.uncc.assessment06.databinding.FragmentCartBinding;
import edu.uncc.assessment06.databinding.CartRowItemBinding;
import edu.uncc.assessment06.databinding.FragmentProductsBinding;

public class CartFragment extends Fragment {

    public CartFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    FragmentCartBinding binding;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    ArrayList<Cart> mCarts = new ArrayList<>();
    CartFragment.CartAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentCartBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }
    ListenerRegistration listenerRegistration;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("My Cart");
        adapter = new CartFragment.CartAdapter();
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.recyclerView.setAdapter(adapter);
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        listenerRegistration = db.collection("cart_items").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Log.w("demo", "Listen failed.", error);
                    return;
                }

                mCarts.clear();
                float total=0;
                for (QueryDocumentSnapshot document : value) {
                    Cart cart = document.toObject(Cart.class);
                    if (cart.getAdded_by().equals(mAuth.getCurrentUser().getUid())) {
                        mCarts.add(cart);
                        total = total + Float.parseFloat(cart.getPrice());
                    }
                }
                binding.textViewTotal.setText(String.valueOf(total));
                adapter.notifyDataSetChanged();


            }
        });
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d("demo", "onDestroyView: ");
        if(listenerRegistration != null){
            listenerRegistration.remove();
        }
    }
    void getCartItems(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("cart_items").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                float total=0;
                if (task.isSuccessful()) {
                    mCarts.clear();
                    total = 0;
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Cart cart = document.toObject(Cart.class);
                        if (cart.getAdded_by().equals(mAuth.getCurrentUser().getUid())) {
                            mCarts.add(cart);
                            total = total + Float.parseFloat(cart.getPrice());
                        }
                    }
                    adapter.notifyDataSetChanged();
                } else {
                    Log.d("demo", "Error getting documents: ", task.getException());
                }
                binding.textViewTotal.setText(String.valueOf(total));
            }
        });
    }
    class CartAdapter extends RecyclerView.Adapter<CartFragment.CartAdapter.CartViewHolder>{

        @NonNull
        @Override
        public CartFragment.CartAdapter.CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new CartFragment.CartAdapter.CartViewHolder(CartRowItemBinding.inflate(getLayoutInflater(), parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull CartFragment.CartAdapter.CartViewHolder holder, int position) {
            Cart cart = mCarts.get(position);
            holder.setupUI(cart);
        }

        @Override
        public int getItemCount() {
            return mCarts.size();
        }

        class CartViewHolder extends RecyclerView.ViewHolder{
            CartRowItemBinding mBinding;
            Cart mCart;
            public CartViewHolder(CartRowItemBinding rowItemBinding) {
                super(rowItemBinding.getRoot());
                mBinding = rowItemBinding;
            }

            void setupUI(Cart cart){
                this.mCart = cart;
                mBinding.textViewProductName.setText(cart.getName());
                mBinding.textViewProductPrice.setText("$" + cart.getPrice());
                Picasso.get().load(cart.getImg_url()).into(mBinding.imageViewProductIcon);
                mBinding.imageViewDeleteFromCart.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        FirebaseFirestore db = FirebaseFirestore.getInstance();
                        db.collection("cart_items").document(mCart.getItem_id()).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                //removed to show the snapshot listener
                                //getPosts();
                            }
                        });
                    }
                });
            }
        }
    }
}