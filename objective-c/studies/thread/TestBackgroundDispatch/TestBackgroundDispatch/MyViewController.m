//
//  MyViewController.m
//  TestBackgroundDispatch
//
//  Created by Mauricio Leal on 2/24/15.
//  Copyright (c) 2015 Mauricio "Maltron" Leal. All rights reserved.
//

#import "MyViewController.h"
#import "SaveBackground.h"

@interface MyViewController ()
@property (nonatomic, strong) UIBarButtonItem *buttonStart, *buttonStop;
@property (nonatomic, strong) SaveBackground *save;
@end

@implementation MyViewController

-(id)init {
    self = [super init];
    if(self) {
        _save = [[SaveBackground alloc] init];
        
    }
    
    return self;
}

- (void)viewDidLoad {
    [super viewDidLoad];
    
    _buttonStart = [[UIBarButtonItem alloc] initWithTitle:@"Start" style:UIBarButtonItemStylePlain target:self action:@selector(actionStart:)];
    _buttonStop = [[UIBarButtonItem alloc] initWithTitle:@"Stop" style:UIBarButtonItemStylePlain target:self action:@selector(actionStop:)];
    [_buttonStop setEnabled:NO];
    [self.navigationItem setLeftBarButtonItem:_buttonStart];
    [self.navigationItem setRightBarButtonItem:_buttonStop];
    [self.navigationItem setTitle:@"Example Dispatch"];
}

-(void)actionStart:(id)sender {
    NSLog(@">>> [MyViewController actionLeft]");
    [self.buttonStart setEnabled:NO]; [self.buttonStop setEnabled:YES];
    [self.save start];
    
//    // SERIAL QUEUE
//    dispatch_queue_t myQueue = dispatch_queue_create("net.nortlam.myQueue",NULL);
//    dispatch_queue_t myConcurrentQueue = dispatch_queue_create("net.nortlam.myConcurrentQueue",
//                                                               DISPATCH_QUEUE_CONCURRENT);
//    
//    // CONCURRENT QUEUE
//    dispatch_queue_t mainQueue = dispatch_get_main_queue();
//    dispatch_queue_t globalHighQueue = dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_HIGH, 0);
//    dispatch_queue_t globalDefaultQueue = dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_DEFAULT, 0);
//    dispatch_queue_t globalLowQueue = dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_LOW, 0);
//    dispatch_queue_t backgroundQueue = dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_BACKGROUND, 0);
    
//    NSLog(@">>> [MyViewController actionLeft] Launching Block #1");
//    dispatch_async(myQueue, ^{
//        for(int i=0; i < 10; i++) {
//            NSLog(@">>> BLOCK #1: %i", i);
//            [NSThread sleepForTimeInterval:2.0f];
//        }
//    });
//    
//    NSLog(@">>> [MyViewController actionLeft] Lauching Block #2");
//    dispatch_async(myQueue, ^{
//        for(int i=0; i < 10; i++) {
//            NSLog(@">>> BLOCK #2: %i", i);
//            [NSThread sleepForTimeInterval:2.0f];
//        }
//    });
}

-(void)actionStop:(id)sender {
    NSLog(@">>> [MyViewController actionRight]");
    [self.buttonStart setEnabled:YES]; [self.buttonStop setEnabled:NO];
    [self.save stop];
}

- (void)didReceiveMemoryWarning {
    NSLog(@">>> [MyViewController didReceiveMemoryWarning]");
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

@end
