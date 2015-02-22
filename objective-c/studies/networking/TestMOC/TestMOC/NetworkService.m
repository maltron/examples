//
//  NetworkService.m
//  TestMOC
//
//  Created by Mauricio Leal on 2/21/15.
//  Copyright (c) 2015 Mauricio "Maltron" Leal. All rights reserved.
//

#import "NetworkService.h"

@implementation NetworkService

-(BOOL)isSuccessfull:(NSHTTPURLResponse *)response {
    if(!response) return NO;
    
    return [response statusCode] >= 200 && [response statusCode] < 300;
}

-(BOOL)isRedirection:(NSHTTPURLResponse *)response {
    if(!response) return NO;
    
    return [response statusCode] >= 300 && [response statusCode] < 400;
}

-(BOOL)isError:(NSHTTPURLResponse *)response {
    if(!response) return NO;
    
    return [response statusCode] >= 400 && [response statusCode] < 500;
}

-(BOOL)isServerError:(NSHTTPURLResponse *)response {
    if(!response) return NO;
    
    return [response statusCode] >= 500;
}

-(NSString *)debug:(NSURL *)url {
    if(!url) return @"NIL";
    
    return [url absoluteString];
}

-(void)logRequest:(NSString *)method httpMethod:(NSString *)httpMethod url:(NSURL *)url data:(NSData *)data {
    NSLog(@">>> %@ REQUEST:<%@> %@", method, httpMethod, [self debug:url]);
    if(data) NSLog(@">>> %@ BODY: %@", method, [[NSString alloc] initWithData:data encoding:NSUTF8StringEncoding]);
}

-(void)logResponse:(NSString *)method response:(NSHTTPURLResponse *)response data:(NSData *)data{
    NSInteger code = [response statusCode];
    NSString *explanation = [NSHTTPURLResponse localizedStringForStatusCode:code];
    NSLog(@"%@ %@ RESPONSE:%li %@", code > 0 ? @">>>" : @"###", method, (long)code, explanation);
    if(data) NSLog(@"%@ BODY:%@", code > 0 ? @">>>" : @"###", [[NSString alloc] initWithData:data encoding:NSUTF8StringEncoding]);
}


@end
